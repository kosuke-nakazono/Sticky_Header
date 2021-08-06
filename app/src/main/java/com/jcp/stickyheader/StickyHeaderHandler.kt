package com.jcp.stickyheader

import android.view.View

interface StickyHeaderHandler {
    companion object {
        const val HEADER_POSITION_NOT_FOUND = -1
    }

    /** StickyHeaderのポジションを返す */
    fun getHeaderPosition(itemPosition: Int): Int
    /** StickyHeaderのレイアウトIDを返す */
    fun getHeaderLayout(headerPosition: Int): Int
    /** StickyHeaderにデーターを渡す */
    fun bindHeaderData(header: View?, headerPosition: Int)
    /** リストがヘッダーかどうかを判定する */
    fun isHeader(itemPosition: Int): Boolean
}