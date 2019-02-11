package liang.lollipop.lsudoku.skin

import android.content.Context
import liang.lollipop.lbaselib.base.BaseFragment

/**
 * Created by lollipop on 2018/3/7.
 * @author Lollipop
 * 包含主题的Fragment
 */
abstract class LSkinFragment: BaseFragment(),LSkinActivity.SkinUpdateCallback {


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if(context != null && context is LSkinActivity){
            context.addSkinUpdateCallback(this)
        }
    }

    override fun onDetach() {
        super.onDetach()
        val thisContext = context
        if(thisContext != null && thisContext is LSkinActivity){
            thisContext.removeSkinUpdateCallback(this)
        }
    }

}