package com.funnyzhao.boxhelper

import com.didichuxing.doraemonkit.kit.AbstractKit

class FloatingBoxManager {
    private val  kits :List<AbstractKit> = arrayListOf()

    private class FloatingBoxManager() {
        companion object {
            val mInstance: FloatingBoxManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
                FloatingBoxManager() }
        }
    }


}