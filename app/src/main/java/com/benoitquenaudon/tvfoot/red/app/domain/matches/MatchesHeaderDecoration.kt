package com.benoitquenaudon.tvfoot.red.app.domain.matches

import android.graphics.Canvas
import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup


class MatchesHeaderDecoration(
    private val matchesAdapter: MatchesAdapter
) : RecyclerView.ItemDecoration() {
  private val headerCache: MutableMap<Long, RecyclerView.ViewHolder> = HashMap()
  private val renderInline: Boolean = true

  override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView,
      state: RecyclerView.State) {
    val position = parent.getChildAdapterPosition(view)
    var headerHeight = 0

    if (position != RecyclerView.NO_POSITION
        && hasHeader(position)
        && showHeaderAboveItem(position)) {

      val header = getHeader(parent, position).itemView
      headerHeight = getHeaderHeightForLayout(header)
    }

    outRect.set(0, headerHeight, 0, 0)
  }

  private fun showHeaderAboveItem(itemAdapterPosition: Int): Boolean {
    return itemAdapterPosition == 0 ||
        matchesAdapter.getHeaderId(itemAdapterPosition - 1) !=
            matchesAdapter.getHeaderId(itemAdapterPosition)
  }

  private fun hasHeader(position: Int): Boolean {
    return matchesAdapter.getHeaderId(position) != NO_HEADER_ID
  }

  private fun getHeader(parent: RecyclerView, position: Int): RecyclerView.ViewHolder {
    val key = matchesAdapter.getHeaderId(position)

    if (headerCache.containsKey(key)) {
      return headerCache[key]!! // :/
    } else {
      val holder = matchesAdapter.onCreateHeaderViewHolder(parent)
      val header = holder.itemView

      matchesAdapter.onBindHeaderViewHolder(holder, position)

      val widthSpec = View.MeasureSpec.makeMeasureSpec(parent.measuredWidth,
          View.MeasureSpec.EXACTLY)
      val heightSpec = View.MeasureSpec.makeMeasureSpec(parent.measuredHeight,
          View.MeasureSpec.UNSPECIFIED)

      val childWidth = ViewGroup.getChildMeasureSpec(widthSpec,
          parent.paddingLeft + parent.paddingRight, header.layoutParams.width)
      val childHeight = ViewGroup.getChildMeasureSpec(heightSpec,
          parent.paddingTop + parent.paddingBottom, header.layoutParams.height)

      header.measure(childWidth, childHeight)
      header.layout(0, 0, header.measuredWidth, header.measuredHeight)

      headerCache.put(key, holder)

      return holder
    }
  }

  companion object Constants {
    val NO_HEADER_ID = -1L
  }

  private fun getHeaderHeightForLayout(header: View): Int {
    return if (renderInline) 0 else header.height
  }

  override fun onDrawOver(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
    val childCount = parent.childCount
    var previousHeaderId: Long = -1

    for (layoutPos in 0 until childCount) {
      val child = parent.getChildAt(layoutPos)
      val adapterPos = parent.getChildAdapterPosition(child)

      if (adapterPos != RecyclerView.NO_POSITION && hasHeader(adapterPos)) {
        val headerId = matchesAdapter.getHeaderId(adapterPos)

        if (headerId != previousHeaderId) {
          previousHeaderId = headerId
          val header = getHeader(parent, adapterPos).itemView
          canvas.save()

          val left = child.left
          val top = getHeaderTop(parent, child, header, adapterPos, layoutPos)
          canvas.translate(left.toFloat(), top.toFloat())

          header.translationX = left.toFloat()
          header.translationY = top.toFloat()
          header.draw(canvas)
          canvas.restore()
        }
      }
    }
  }

  private fun getHeaderTop(parent: RecyclerView, child: View, header: View, adapterPos: Int,
      layoutPos: Int): Int {
    val headerHeight = getHeaderHeightForLayout(header)
    var top = child.y.toInt() - headerHeight
    if (layoutPos == 0) {
      val count = parent.childCount
      val currentId = matchesAdapter.getHeaderId(adapterPos)
      // find next view with header and compute the offscreen push if needed
      for (i in 1 until count) {
        val adapterPosHere = parent.getChildAdapterPosition(parent.getChildAt(i))
        if (adapterPosHere != RecyclerView.NO_POSITION) {
          val nextId = matchesAdapter.getHeaderId(adapterPosHere)
          if (nextId != currentId) {
            val next = parent.getChildAt(i)
            val offset = next.y.toInt() - (headerHeight + getHeader(parent,
                adapterPosHere).itemView.height)
            return if (offset < 0) {
              offset
            } else {
              break
            }
          }
        }
      }

      top = Math.max(0, top)
    }

    return top
  }
}