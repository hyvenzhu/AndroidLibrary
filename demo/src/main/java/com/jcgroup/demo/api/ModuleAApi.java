package com.jcgroup.demo.api;

import com.jcgroup.demo.api.res.ListCategory;
import com.jcgroup.demo.base.InfoResult;

import io.reactivex.Observable;
import retrofit2.http.POST;

/**
 * @author zhuhf
 * @version [DX-AndroidLibrary, 2018-03-07]
 */
public interface ModuleAApi {
    @POST("api/shop/v4/group/category/list")
    Observable<InfoResult<ListCategory>> categoryList();
}
