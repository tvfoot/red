package com.benoitquenaudon.tvfoot.red.app.domain.matches

import android.graphics.Canvas
import android.graphics.Rect
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import androidx.core.util.getOrDefault
import androidx.recyclerview.widget.RecyclerView
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesItemViewHolder.MatchHeaderViewHolder

class MatchesHeaderDecoration(
    private val matchesAdapter: MatchesAdapter
) : RecyclerView.ItemDecoration() {
  private val headerViewHolderCache: SparseArray<MatchHeaderViewHolder> = SparseArray()

  override fun getItemOffsets(
      outRect: Rect,
      view: View,
      parent: RecyclerView,
      state: RecyclerView.State
  ) {
    val position = parent.getChildAdapterPosition(view)
    if (position != RecyclerView.NO_POSITION && showHeaderAboveItem(position)) {
      val headerHeight = getHeaderHeightForLayout(getHeader(parent, position).itemView)
      outRect.set(0, headerHeight, 0, 0)
      return
    }

    outRect.set(0, 0, 0, 0)
  }

  private fun showHeaderAboveItem(adapterPosition: Int): Boolean =
      adapterPosition == 0 || !sameHeaderAt(adapterPosition - 1, adapterPosition)

  private fun sameHeaderAt(firstPosition: Int, secondPosition: Int): Boolean =
      matchesAdapter.closestHeaderPosition(firstPosition) ==
          matchesAdapter.closestHeaderPosition(secondPosition)

  private fun getHeader(parent: RecyclerView, position: Int): RecyclerView.ViewHolder {
    val headerPosition = matchesAdapter.closestHeaderPosition(position)

    val holder = headerViewHolderCache.getOrDefault(
        headerPosition,
        (matchesAdapter.onCreateViewHolder(parent, matchesAdapter.getItemViewType(headerPosition))
            as MatchHeaderViewHolder).also { headerViewHolderCache.put(headerPosition, it) })

    matchesAdapter.onBindViewHolder(holder, headerPosition)

    val widthSpec =
        View.MeasureSpec.makeMeasureSpec(parent.measuredWidth, View.MeasureSpec.EXACTLY)
    val heightSpec =
        View.MeasureSpec.makeMeasureSpec(parent.measuredHeight, View.MeasureSpec.UNSPECIFIED)

    holder.itemView.let { headerView ->
      val childWidth = ViewGroup.getChildMeasureSpec(widthSpec,
          parent.paddingLeft + parent.paddingRight, headerView.layoutParams.width)
      val childHeight = ViewGroup.getChildMeasureSpec(heightSpec,
          parent.paddingTop + parent.paddingBottom, headerView.layoutParams.height)

      headerView.measure(childWidth, childHeight)
      headerView.layout(0, 0, headerView.measuredWidth, headerView.measuredHeight)
    }

    return holder
  }

  @Suppress("UNUSED_PARAMETER")
  private fun getHeaderHeightForLayout(header: View): Int {
//    return if (renderInline) 0 else header.height
    return 0
  }

  override fun onDrawOver(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
    val childCount = parent.childCount
    var previousHeaderPosition: Int = -1

    for (layoutPos in 0 until childCount) {
      val child = parent.getChildAt(layoutPos)
      val adapterPos = parent.getChildAdapterPosition(child)

      if (adapterPos != RecyclerView.NO_POSITION) {
        val headerPosition = matchesAdapter.closestHeaderPosition(adapterPos)

        if (headerPosition != previousHeaderPosition) {
          previousHeaderPosition = headerPosition
          val header = getHeader(parent, adapterPos).itemView
          canvas.save()

          val left = child.left
          val top = getHeaderTop(parent, child, header, layoutPos, headerPosition)
          canvas.translate(left.toFloat(), top.toFloat())

          header.translationX = left.toFloat()
          header.translationY = top.toFloat()
          header.draw(canvas)
          canvas.restore()
        }
      }
    }
  }

  private fun getHeaderTop(
      parent: RecyclerView,
      child: View,
      header: View,
      layoutPos: Int,
      headerPosition: Int
  ): Int {
    val headerHeight = getHeaderHeightForLayout(header)
    var top = child.y.toInt() - headerHeight
    if (layoutPos == 0) {
      val count = parent.childCount
      // find next view with header and compute the offscreen push if needed
      for (i in 1 until count) {
        val adapterPosHere = parent.getChildAdapterPosition(parent.getChildAt(i))
        if (adapterPosHere != RecyclerView.NO_POSITION) {
          val nextHeaderPosition = matchesAdapter.closestHeaderPosition(adapterPosHere)
          if (nextHeaderPosition != headerPosition) {
            val next = parent.getChildAt(i)
            val offset =
                next.y.toInt() - (headerHeight + getHeader(parent, adapterPosHere).itemView.height)
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