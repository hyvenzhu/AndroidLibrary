package android.demo.api;

import android.demo.api.res.ListCategory;
import android.demo.base.InfoResult;

import io.reactivex.Observable;
import retrofit2.http.POST;

/**
 * @author zhuhf
 * @version [AndroidLibrary, 2018-03-07]
 */
public interface ModuleAApi {
    @POST("api/shop/v4/group/category/list")
    Observable<InfoResult<ListCategory>> categoryList();
}
