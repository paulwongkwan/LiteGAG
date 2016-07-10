
package mememe.litegag.object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Paging {

    @SerializedName("next")
    @Expose
    private Object next;

    /**
     * 
     * @return
     *     The next
     */
    public Object getNext() {
        return next;
    }

    /**
     * 
     * @param next
     *     The next
     */
    public void setNext(Object next) {
        this.next = next;
    }

}
