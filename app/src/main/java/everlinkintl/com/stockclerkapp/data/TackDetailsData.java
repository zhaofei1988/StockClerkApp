package everlinkintl.com.stockclerkapp.data;

public class TackDetailsData {
    private String head;
    private String body;
    private String type;
    private String address;
    private boolean isSelected=false;
    private boolean childIsShow= true;
    private NewTaskDetailsData newTaskDetailsData;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isChildIsShow() {
        return childIsShow;
    }

    public void setChildIsShow(boolean childIsShow) {
        this.childIsShow = childIsShow;
    }

    public NewTaskDetailsData getNewTaskDetailsData() {
        return newTaskDetailsData;
    }

    public void setNewTaskDetailsData(NewTaskDetailsData newTaskDetailsData) {
        this.newTaskDetailsData = newTaskDetailsData;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

}
