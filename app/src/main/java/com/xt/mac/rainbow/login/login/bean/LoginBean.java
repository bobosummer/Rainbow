package com.xt.mac.rainbow.login.login.bean;

public class LoginBean {

    /**
     * content : {"id":914,"userName":"zhaoxuegang","realName":"赵学刚"}
     * respCode : success
     * respMsg : 登录成功！
     */

    private ContentBean content;
    private String respCode;
    private String respMsg;

    public ContentBean getContent() {
        return content;
    }

    public void setContent(ContentBean content) {
        this.content = content;
    }

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public String getRespMsg() {
        return respMsg;
    }

    public void setRespMsg(String respMsg) {
        this.respMsg = respMsg;
    }

    public static class ContentBean {
        /**
         * id : 914
         * userName : zhaoxuegang
         * realName : 赵学刚
         */

        private int id;
        private String userName;
        private String realName;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }
    }
}
