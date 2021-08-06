package com.jcp.stickyheader

import android.graphics.Canvas
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class MyItemDecoration(private val mStickyHeaderListener: StickyHeaderHandler) : RecyclerView.ItemDecoration() {

    // Header View
    private var mCurrentHeaderView: View? = null

    // RecyclerViewのセルが表示される度に呼ばれる
    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)

        // 表示中のリスト内のトップViewを取得
        val topChildView: View = parent.getChildAt(0)?:return

        // topChildViewのインデックス
        val topChildViewPosition = parent.getChildAdapterPosition(topChildView)

        // topChildViewPosition取得失敗時はこれ以降の処理を行わない
        if (topChildViewPosition == RecyclerView.NO_POSITION) return

        // 直近のHeaderPositionのインデックスを取得
        val prevHeaderPosition = mStickyHeaderListener.getHeaderPosition(topChildViewPosition)

        // 直近にHeaderが存在しない場合はこれ以降の処理を行わない
        if(prevHeaderPosition == StickyHeaderHandler.HEADER_POSITION_NOT_FOUND) return

        // 直近にHeaderが存在する → topChildViewPositionにHeaderが存在する
        mCurrentHeaderView = getHeaderView(topChildViewPosition, parent)

        // 現在のHeaderレイアウトのセット
        fixLayoutSize(parent, mCurrentHeaderView)

        // 現在のHeaderのBottom Positionを取得 (親Viewからの相対距離)
        val contactPoint = mCurrentHeaderView!!.bottom

        // Headerの次のセルを取得
        val nextCell = getNextCellToHeader(parent, contactPoint) ?: return  // 次のセルがない
        // nextCellのインデックスを取得
        val nextCellPosition = parent.getChildAdapterPosition(nextCell)
        if(nextCellPosition == StickyHeaderHandler.HEADER_POSITION_NOT_FOUND) return

        // nextCellがHeaderかどうかの判定
        if (mStickyHeaderListener.isHeader(nextCellPosition)) {
            // 既存のStickyヘッダーを押し上げる
            moveHeader(c, mCurrentHeaderView, nextCell)
            return
        }

        // Stickyヘッダーの描画
        drawHeader(c, mCurrentHeaderView)
    }

    // HeaderのViewを取得
    private fun getHeaderView(itemPosition: Int, parent: RecyclerView): View? {
        val headerPosition = mStickyHeaderListener.getHeaderPosition(itemPosition)
        val layoutResId = mStickyHeaderListener.getHeaderLayout(headerPosition)
        // Headerレイアウトをinflate
        val headerView = LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)
        // データバインディング
        mStickyHeaderListener.bindHeaderData(headerView, headerPosition)
        return headerView
    }

    // Headerレイアウトのセット
    private fun fixLayoutSize(parent: ViewGroup, headerView: View?) {
        headerView?:return

        // RecyclerViewのSpecを取得
        val widthSpec = View.MeasureSpec.makeMeasureSpec(parent.width, View.MeasureSpec.EXACTLY)
        val heightSpec = View.MeasureSpec.makeMeasureSpec(parent.height, View.MeasureSpec.UNSPECIFIED)

        // Header ViewのSpecを取得
        val headerWidthSpec = ViewGroup.getChildMeasureSpec(
                widthSpec,
                parent.paddingLeft + parent.paddingRight,
                headerView.layoutParams.width
        )
        val headerHeightSpec = ViewGroup.getChildMeasureSpec(
                heightSpec,
                parent.paddingTop + parent.paddingBottom,
                headerView.layoutParams.height
        )
        headerView.measure(headerWidthSpec, headerHeightSpec)
        headerView.layout(0, 0, headerView.measuredWidth, headerView.measuredHeight)
    }


    // Headerの次のセルを取得
    private fun getNextCellToHeader(parent: RecyclerView, contactPoint: Int): View? {
        var nextView: View? = null
        for (index in 0 until parent.childCount) {
            val child = parent.getChildAt(index)
            if (child.bottom > contactPoint) {
                if (child.top <= contactPoint) {
                    nextView = child
                    break
                }
            }
        }
        return nextView
    }

    // Stickyヘッダーを動かす
    private fun moveHeader(c: Canvas, currentHeader: View?, nextCell: View) {
        currentHeader?:return

        c.save()
        c.translate(0F, (nextCell.top - currentHeader.height).toFloat())
        currentHeader.draw(c)
        c.restore()
    }

    // Stickyヘッダーを描画する
    private fun drawHeader(c: Canvas, header: View?) {
        c.save()
        c.translate(0F, 0F)
        header!!.draw(c)
        c.restore()
    }
}