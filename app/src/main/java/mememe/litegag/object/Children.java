
package mememe.litegag.object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Children {

    @SerializedName("data")
    @Expose
    private List<Data> data = new ArrayList<>();
    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("paging")
    @Expose
    private Paging paging;

    /**
     *
     * @return
     *     The data
     */
    public List<Data> getData() {
        return data;
    }

    /**
     *
     * @param data
     *     The data
     */
    public void setData(List<Data> data) {
        this.data = data;
    }

    /**
     * 
     * @return
     *     The count
     */
    public Integer getCount() {
        return count;
    }

    /**
     * 
     * @param count
     *     The count
     */
    public void setCount(Integer count) {
        this.count = count;
    }

    /**
     * 
     * @return
     *     The paging
     */
    public Paging getPaging() {
        return paging;
    }

    /**
     * 
     * @param paging
     *     The paging
     */
    public void setPaging(Paging paging) {
        this.paging = paging;
    }

}
