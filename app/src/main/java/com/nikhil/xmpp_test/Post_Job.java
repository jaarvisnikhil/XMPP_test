package com.nikhil.xmpp_test;


public class Post_Job {

    String status;
    static Post_Job mObj = null;

    public Post_Job() {
    }

    public static Post_Job getInstance() {
        if (mObj == null) {
            mObj = new Post_Job();
        }
        return mObj;
    }
    /**
     * This returns status
     */
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
