package com.soft.permission;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.jakewharton.rxbinding2.view.RxView;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Manifest.permission.CAMERA,    拍照权限
 * Manifest.permission.WRITE_EXTERNAL_STORAGE,   //sdcard写权限
 * Manifest.permission.READ_EXTERNAL_STORAGE     //sdcard读权限
 * 描述：权限帮助类
 * 作者：shaobing
 * 时间： 2017/4/11 19:31
 */
public class PermissionManager {

   private static  RxPermissions rxPermissions;

    /**
     * 实例化
     * @param activity
     * @return
     */
   public static RxPermissions getInstance(Activity activity){
       if(rxPermissions ==null){
           rxPermissions = new RxPermissions(activity); // where this is an Activity instance
       }
       return rxPermissions;
   }


    /**
     * 请求权限，显示合并的接口
     * @param listener  回调
     * @param permissions  需要申请的权限
     */
    public static void  requestPermission(final OnPermissionListener listener,final String... permissions){
        rxPermissions.request(permissions)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>(){
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean granted) {
                        if(listener!=null){
                            if(granted){
                                listener.onGranted();
                            }else{
                                listener.onDenied();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if(listener!=null){
                            listener.onDenied();
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }


    /**
     * 基本不用
     * 单独请求权限
     * @param listener  回调
     * @param permissions  需要申请的权限
     */
    public static void  requestPermissionByEach(final OnPermissionListener listener,final String... permissions){

        rxPermissions
                .requestEach(Manifest.permission.CAMERA, Manifest.permission.READ_PHONE_STATE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Permission>(){
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Permission permission) {
                        if(listener!=null){
                            if (permission.granted) {
                                listener.onGranted();
                            } else if (permission.shouldShowRequestPermissionRationale){
                                listener.onDenied();
                            }
                            else {
                                listener.onDenied();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if(listener!=null){
                            listener.onDenied();
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }


    /**
     * View 点击权限
     * @param view
     * @param listener
     * @param permissions
     */
    public static void  onClickPermission(View view, final OnPermissionListener listener, final String... permissions){
        RxView.clicks(view)
                .compose(rxPermissions.ensure(permissions))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean granted) {
                        if(listener!=null){
                            if(granted){
                                listener.onGranted();
                            }else{
                                listener.onDenied();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if(listener!=null){
                            listener.onDenied();
                        }
                    }

                    @Override
                    public void onComplete() {
                    }
                });

    }

    /**
     * 权限接口
     */
    public interface OnPermissionListener{

        void onGranted();

        void onDenied();
    }







}
