package com.flexible

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import androidx.annotation.FloatRange
import androidx.annotation.Px
import androidx.core.view.ViewCompat
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import kotlin.math.abs
import kotlin.math.max

internal fun MaterialShapeDrawable.stateful(block: MaterialShapeDrawable.() -> Unit) {
  if (isStateful) {
    block(this)
  }
}

internal fun MaterialShapeDrawable.setBounds(radius: Int) {
  shapeAppearanceModel = ShapeAppearanceModel.builder()
    .setAllCorners(CornerFamily.ROUNDED, radius.toFloat())
    .build()
  setBounds(0, 0, radius * 2, radius * 2)
}

internal fun List<Float>.approximate(value: Float): Float {
  var diff: Float
  var minDiff = Float.MAX_VALUE
  var approximate = Float.NaN

  for (idx in indices) {
    diff = abs(get(idx) - value)
    if (minDiff >= diff) {
      minDiff = diff
      approximate = get(idx)
    }
  }

  return approximate
}

internal fun List<Float>.clapped(
  idx: Int,
  minimumValue: Float,
  maximumValue: Float,
  minimumRange: Float,
  value: Float
): Float {
  val upperBound = getOrNull(idx + 1) ?: maximumValue
  val lowerBound = getOrNull(idx - 1) ?: minimumValue

  return if (idx == 0) {
    if (lowerBound > upperBound - minimumRange) return lowerBound
    value.coerceIn(lowerBound, upperBound - minimumRange)
  } else {
    if (lowerBound + minimumRange > upperBound) return lowerBound + minimumRange
    value.coerceIn(lowerBound + minimumRange, upperBound)
  }
}

internal fun View.canScrollVertically(): Boolean {
  var view = parent
  while (view is ViewGroup) {
    val canScrollVertically = view.canScrollVertically(1) || view.canScrollVertically(-1)
    if (canScrollVertically && view.shouldDelayChildPressedState()) {
      return true
    }
    view = view.parent
  }
  return false
}

internal fun lerp(
  startValue: Float,
  endValue: Float,
  @FloatRange(from = 0.0, to = 1.0) fraction: Float
): Float = startValue + fraction * (endValue - startValue)



class AppFlexibleStepRangeSlider @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

  private val inactiveTrackPaint: Paint = Paint().apply {
    this.style = Paint.Style.STROKE
    this.strokeCap = Paint.Cap.ROUND
  }
  private val activeTrackPaint: Paint = Paint().apply {
    this.style = Paint.Style.STROKE
  }

  private val inactiveTickPaint: Paint = Paint().apply { this.style = Paint.Style.STROKE }
  private val activeTickPaint: Paint = Paint().apply { this.style = Paint.Style.STROKE }

  private val inactiveThumbDrawable = MaterialShapeDrawable().apply {
    shadowCompatibilityMode = MaterialShapeDrawable.SHADOW_COMPAT_MODE_ALWAYS
  }
  private val activeThumbDrawable = MaterialShapeDrawable().apply {
    shadowCompatibilityMode = MaterialShapeDrawable.SHADOW_COMPAT_MODE_ALWAYS
  }

  private var inactiveTrackColor: ColorStateList? = null
  private var activeTrackColor: ColorStateList? = null
  private var inactiveTickColor: ColorStateList? = null
  private var activeTickColor: ColorStateList? = null

  private var inactiveThumbRadius: Int = 0
  private var activeThumbRadius: Int = 0
  private val maxThumbRadius: Int get() = max(inactiveThumbRadius, activeThumbRadius)

  private var trackWidth: Int = 0
  private var trackHeight: Int = 0
  private var trackSidePadding: Int = 0
  var minimumRange: Float = 0f

  private val scaledTouchSlop: Int = ViewConfiguration.get(context).scaledTouchSlop

  private var touchDownX: Float = 0f
  private var touchPosition: Float = 0f
  private var lastEvent: MotionEvent? = null
  private var isThumbPressed: Boolean = false

  private var minValue: Float = NO_VALUE
  private var maxValue: Float = NO_VALUE

  /**
   * The value of [valueFrom, valueTo] range in the actual slider.
   * This initiate by [setValues] function.
   *
   * Values only have two value that valueFrom and valueTo.
   * [valueFrom] is value of first actual slider.
   * [valueTo] is value of last actual slider.
   *
   * Example) values = listOf(0, 200)
   * ㅁ------------ㅁ -> (0,200)
   * ------ㅁ------ㅁ -> (100, 200)
   * --------------ㅁ -> (200, 200)
   *
   * Example) values = listOf(0, 50, 100, 200)
   * ㅁ---I----I---------ㅁ -> (0,   200)
   * I---ㅁ----ㅁ---------- -> (50,  100)
   * I---I----I----------ㅁ -> (200, 200)
   */
  private var values: MutableList<Float> = mutableListOf()

  /**
   * [values] and parameter of [setValues] is different because of set internally.
   */
  private var valuesCached: List<Float> = emptyList()

  /**
   * The positions of percentages to indicate where the snap is stopped
   *
   * Example)
   * values [0f, 10f, 50f, 100f, 200f] -> snapPosition [0.0f, 0.05f, 0.25f, 0.5f, 1.0f]
   */
  private var snapPositions: List<Float> = emptyList()

  private var activeThumbIdx: Int = NO_INDEX

  /**
   * Visibility of ticks
   */
  var isTickVisible: Boolean = false
    set(value) {
      if (field == value) {
        return
      }
      field = value
      postInvalidate()
    }

  val valueFrom: Float get() = values.firstOrNull() ?: NO_VALUE
  val valueTo: Float get() = values.lastOrNull() ?: NO_VALUE

  private val listeners: MutableList<OnValueChangeListener> = mutableListOf()

  enum class ValueChangeState {
    Dragging,
    Idle
  }

  interface OnValueChangeListener {
    fun onValueChange(activeThumbIdx: Int, from: Float, to: Float, state: ValueChangeState)
    fun onTouchUp()
  }

  init {

    isFocusable = true
    isClickable = true
  }

  fun update() {
    updateDrawableState()
    updateTrackSidePadding()
    updateStrokeWidth()

    if (isInEditMode) {
      activeThumbIdx = 0
      if (values.isEmpty()) {
        setValues(listOf(0f, 100f))
      }
    }
  }


  fun setTrackColorInactive(colors: ColorStateList) {
    this.inactiveTrackColor = colors
    inactiveTrackPaint.color = colors.colorForState

    postInvalidate()
  }

  fun setTrackColorActive(colors: ColorStateList) {
    this.activeTrackColor = colors
    activeTrackPaint.color = colors.colorForState

    postInvalidate()
  }

  fun setTrackHeight(@Px height: Int) {
    this.trackHeight = height

    updateStrokeWidth()
    requestLayout()
  }

  fun setThumbRadiusActive(@Px radius: Int) {
    this.activeThumbRadius = radius
    activeThumbDrawable.setBounds(radius)

    updateTrackSidePadding()
    requestLayout()
  }

  fun setThumbRadiusInactive(@Px radius: Int) {
    this.inactiveThumbRadius = radius
    inactiveThumbDrawable.setBounds(radius)

    updateTrackSidePadding()
    requestLayout()
  }

  fun setThumbFillColorActive(colors: ColorStateList) {
    activeThumbDrawable.fillColor = colors

    postInvalidate()
  }

  fun setThumbFillColorInactive(colors: ColorStateList) {
    inactiveThumbDrawable.fillColor = colors

    postInvalidate()
  }

  fun setThumbStrokeColorActive(colors: ColorStateList) {
    activeThumbDrawable.strokeColor = colors

    postInvalidate()
  }

  fun setThumbStrokeColorInactive(colors: ColorStateList) {
    inactiveThumbDrawable.strokeColor = colors

    postInvalidate()
  }

  fun setThumbStrokeWidthActive(width: Float) {
    activeThumbDrawable.strokeWidth = width

    postInvalidate()
  }

  fun setThumbStrokeWidthInactive(width: Float) {
    inactiveThumbDrawable.strokeWidth = width

    postInvalidate()
  }

  fun setThumbElevationActive(@Px elevation: Float) {
    activeThumbDrawable.elevation = elevation

    postInvalidate()
  }

  fun setThumbElevationInactive(@Px elevation: Float) {
    inactiveThumbDrawable.elevation = elevation

    postInvalidate()
  }

  fun setTickColorActive(colors: ColorStateList) {
    activeTickColor = colors
    activeTickPaint.color = colors.colorForState

    postInvalidate()
  }

  fun setTickColorInactive(colors: ColorStateList) {
    inactiveTickColor = colors
    inactiveTickPaint.color = colors.colorForState

    postInvalidate()
  }

  private fun updateDrawableState() {
    inactiveTrackColor?.colorForState?.let { inactiveTrackPaint.color = it }
    activeTrackColor?.colorForState?.let { activeTrackPaint.color = it }
    inactiveTickColor?.colorForState?.let { inactiveTickPaint.color = it }
    activeTickColor?.colorForState?.let { activeTickPaint.color = it }
    activeThumbDrawable.stateful { state = drawableState }
    inactiveThumbDrawable.stateful { state = drawableState }
  }

  private fun updateTrackSidePadding() {
    trackSidePadding = max(maxThumbRadius, max(paddingStart, paddingEnd))
    if (ViewCompat.isLaidOut(this)) {
      updateTrackWidth(width)
    }
  }

  private fun updateTrackWidth(width: Int) {
    trackWidth = max(width - (trackSidePadding * 2), 0)
  }

  private fun updateStrokeWidth() {
    val width = trackHeight.toFloat()
    inactiveTrackPaint.strokeWidth = width
    activeTrackPaint.strokeWidth = width
    inactiveTickPaint.strokeWidth = width / 2.0f
    activeTickPaint.strokeWidth = width / 2.0f
  }

  fun addOnValueChangeListener(listener: OnValueChangeListener) {
    listeners.add(listener)
  }

  fun removeOnValueChangeListener(listener: OnValueChangeListener) {
    listeners.remove(listener)
  }

  fun clearOnValueChangeListeners() {
    listeners.clear()
  }

  fun setValues(
    values: List<Float>,
    valueFrom: Float? = null,
    valueTo: Float? = null
  ) {
    when {
      values.isEmpty() -> {
        release()
        return
      }
      values.size == 1 -> error("At least two or more values must be set")
      values == this.values -> return
    }
    this.valuesCached = values
    setValuesInternal(values.sorted().distinct(), valueFrom, valueTo)
  }

  private fun setValuesInternal(
    values: List<Float>,
    initialValueFrom: Float?,
    initialValueTo: Float?
  ) {
    val (min, max) = values.first() to values.last()
    this.minValue = min
    this.maxValue = max
    this.snapPositions = values.normalized()

    val valueFrom = initialValueFrom?.coerceIn(min, max) ?: min
    val valueTo = initialValueTo?.coerceIn(min, max) ?: max
    if (values.size > 2) {
      this.values = mutableListOf(
        values.approximate(valueFrom),
        values.approximate(valueTo)
      )
    } else {
      this.values = mutableListOf(valueFrom, valueTo)
    }
    notifyValueChanged(ValueChangeState.Idle)
    postInvalidate()
  }

  private fun release() {
    this.minValue = NO_VALUE
    this.maxValue = NO_VALUE
    this.values = mutableListOf()
    this.valuesCached = emptyList()
    this.activeThumbIdx = NO_INDEX
    this.snapPositions = emptyList()
    invalidate()
  }

  private fun notifyValueChanged(state: ValueChangeState) = listeners.forEach { listener ->
    listener.onValueChange(activeThumbIdx, valueFrom, valueTo, state)
  }

  private fun onTouchUp() = listeners.forEach { listener ->
    listener.onTouchUp()
  }

  override fun onDraw(canvas: Canvas) {
    val top: Float = (paddingTop + maxThumbRadius).toFloat()
    drawInactiveTrack(canvas, trackWidth, top)

    val lastValue = values.lastOrNull() ?: return
    if (lastValue > minValue) {
      drawActiveTrack(canvas, trackWidth, top)
    }

    drawTicks(canvas, top)
    drawThumbs(canvas, trackWidth, top)
  }

  private fun drawInactiveTrack(canvas: Canvas, width: Int, top: Float) {
    fun drawLine(startX: Number, stopX: Number) =
      canvas.drawLine(startX.toFloat(), top, stopX.toFloat(), top, inactiveTrackPaint)

    // If no values draws inactive track only
    val activeRange = getActiveRange()
    if (activeRange == null) {
      drawLine(trackSidePadding, trackSidePadding + width)
      return
    }
    val left = trackSidePadding + activeRange[0] * width
    if (left > trackSidePadding) {
      drawLine(trackSidePadding, left)
    }
    val right = trackSidePadding + activeRange[1] * width
    if (right < trackSidePadding + width) {
      drawLine(right, (trackSidePadding + width))
    }
  }

  private fun drawActiveTrack(canvas: Canvas, width: Int, top: Float) {
    val activeRange = getActiveRange() ?: return
    val left = trackSidePadding + activeRange[0] * width
    val right = trackSidePadding + activeRange[1] * width
    canvas.drawLine(left, top, right, top, activeTrackPaint)
  }

  private fun getActiveRange(): FloatArray? {
    if (values.isEmpty()) {
      return null
    }
    val left = values.first().normalized()
    val right = values.last().normalized()
    return floatArrayOf(left, right)
  }

  private fun drawTicks(canvas: Canvas, y: Float) {
    if (!isTickVisible) {
      return
    }
    val activeRange = getActiveRange() ?: return
    snapPositions.forEach { position ->
      val x = trackSidePadding + (trackWidth.toFloat() * position)
      val paint = if (position >= activeRange[0] && position <= activeRange[1]) {
        activeTickPaint
      } else {
        inactiveTickPaint
      }
      canvas.drawPoint(x, y, paint)
    }
  }

  private fun drawThumbs(canvas: Canvas, width: Int, top: Float) =
    values.forEachIndexed { index, value ->
      val isActive = index == activeThumbIdx
      val radius = if (isActive) activeThumbRadius else inactiveThumbRadius
      val x = trackSidePadding + (value.normalized() * width) - radius
      val y = top - radius
      canvas.withTranslation(x, y) {
        if (isActive) {
          activeThumbDrawable.draw(canvas)
        } else {
          inactiveThumbDrawable.draw(canvas)
        }
      }
    }

  @SuppressLint("ClickableViewAccessibility")
  override fun onTouchEvent(event: MotionEvent): Boolean {
    if (!isEnabled || values.isEmpty()) {
      return false
    }
    val x = event.x
    touchPosition = ((x - trackSidePadding) / trackWidth).coerceIn(0f, 1f)

    when (event.actionMasked) {
      MotionEvent.ACTION_DOWN -> run {
        touchDownX = x
        if (canScrollVertically()) {
          return@run
        }
        parent.requestDisallowInterceptTouchEvent(true)
        if (!pickActiveThumb()) {
          return@run
        }
        requestFocus()
        isThumbPressed = true

        snapPosition(touchPosition)
        invalidate()
      }
      MotionEvent.ACTION_MOVE -> run {
        if (!isThumbPressed) {
          if (canScrollVertically() && abs(x - touchDownX) < scaledTouchSlop) {
            return false
          }
          parent.requestDisallowInterceptTouchEvent(true)
        }
        if (!pickActiveThumb()) {
          return@run
        }
        isThumbPressed = true
        if (snapPosition(touchPosition)) {
          notifyValueChanged(ValueChangeState.Dragging)
        }
        invalidate()
      }
      MotionEvent.ACTION_UP -> {
        isThumbPressed = false
        lastEvent?.let { lastEvent ->
          if (lastEvent.actionMasked == MotionEvent.ACTION_DOWN &&
            abs(lastEvent.x - event.y) <= scaledTouchSlop &&
            abs(lastEvent.y - event.y) <= scaledTouchSlop
          ) {
            pickActiveThumb()
          }
        }
        if (activeThumbIdx != NO_INDEX) {
          snapPosition(touchPosition)
          activeThumbIdx = NO_INDEX
        }
        notifyValueChanged(ValueChangeState.Idle)
        onTouchUp()
        invalidate()
      }
    }
    isPressed = isThumbPressed
    lastEvent = MotionEvent.obtain(event)
    return true
  }

  /**
   * Pick thumb by touch position.
   */
  private fun pickActiveThumb(): Boolean {
    if (activeThumbIdx != NO_INDEX) {
      return true
    }
    activeThumbIdx = 0
    val touchValue: Float = positionToValue(touchPosition)
    val touchX: Float = valueToX(touchValue)

    val valueFromX = valueToX(valueFrom)
    val valueFromDiff = abs(valueFromX - touchX)
    val valueToX = valueToX(valueTo)
    val valueToDiff = abs(valueToX - touchX)
    val diff = valueFromDiff.compareTo(valueToDiff)

    when {
      diff > 0 -> activeThumbIdx = 1
      diff == 0 -> {
        if (abs(valueToX - touchX) < scaledTouchSlop) {
          activeThumbIdx = NO_INDEX
          return false
        }
        if ((valueToX - touchX) < 0) {
          activeThumbIdx = 1
        }
      }
    }
    return activeThumbIdx != NO_INDEX
  }

  /**
   * Convert position to value
   * - position: percentage of slider
   * - value: value of thumb
   */
  private fun positionToValue(position: Float): Float = lerp(minValue, maxValue, position)

  /**
   * Convert value to x
   * - value: value of thumb
   * - x: x of View
   */
  private fun valueToX(value: Float): Float = (value.normalized() * trackWidth) + trackSidePadding

  private fun snapPosition(position: Float): Boolean {
    val approximateValue = positionToValue(approximatePosition(position))
    return snapToValue(activeThumbIdx, approximateValue)
  }

  /**
   * Get approximate position in snap positions.
   * If snap positions is empty, return input position
   */
  private fun approximatePosition(position: Float): Float = snapPositions
    .takeIf { it.size > 2 }
    ?.approximate(position)
    ?: position

  private fun snapToValue(idx: Int, value: Float): Boolean {
    if (abs(value - values[idx]) < THRESHOLD) {
      return false
    }

    val newValue = values.clapped(idx, minValue, maxValue, minimumRange, value)
    if (values[idx] == newValue) {
      return false
    }
    values[idx] = if (valuesCached.size > 2) {
      valuesCached.approximate(newValue)
    } else {
      newValue
    }
    return true
  }

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    val paddingVertical = paddingTop + paddingBottom
    val height = max(trackHeight, maxThumbRadius * 2) + paddingVertical
    super.onMeasure(
      widthMeasureSpec,
      MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
    )
  }

  override fun drawableStateChanged() {
    super.drawableStateChanged()
    updateDrawableState()
  }

  override fun getPaddingStart(): Int =
    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
      super.getPaddingStart()
    } else {
      // Don't support RTL
      paddingLeft
    }

  override fun getPaddingEnd(): Int {
    return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
      super.getPaddingEnd()
    } else {
      // Don't support RTL
      paddingRight
    }
  }

  override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
    super.setPadding(left, top, right, bottom)

    updateTrackSidePadding()
    postInvalidate()
  }

  override fun setPaddingRelative(start: Int, top: Int, end: Int, bottom: Int) {
    super.setPaddingRelative(start, top, end, bottom)

    updateTrackSidePadding()
    postInvalidate()
  }

  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    updateTrackWidth(w)
  }

  private fun List<Float>.normalized(): List<Float> = if (size < 2) {
    emptyList()
  } else {
    map { value -> value.normalized() }
  }

  private fun Float.normalized(): Float = (this - minValue) / (maxValue - minValue)

  private inline val ColorStateList.colorForState: Int
    get() = getColorForState(drawableState, defaultColor)

  companion object {
    private const val THRESHOLD: Double = .0001
    private const val KEY_SUPER_STATE = "KEY_SUPER_STATE"
    private const val KEY_VALUES = "KEY_VALUES"

    const val NO_VALUE: Float = Float.MIN_VALUE
    const val NO_INDEX: Int = -1
  }
}

