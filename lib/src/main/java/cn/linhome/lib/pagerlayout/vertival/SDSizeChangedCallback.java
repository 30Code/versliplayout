package cn.linhome.lib.pagerlayout.vertival;

/**
 * des:
 * Created by 30Code
 * on 2018/11/21
 */
public interface SDSizeChangedCallback<T>
{
    void onWidthChanged(int newWidth, int oldWidth, T target);

    void onHeightChanged(int newHeight, int oldHeight, T target);
}
