package osa.ora.beans;

import java.io.Serializable;
/*
 * @author Osama Oransa
 */
public class Action implements Serializable {

    private String action;
    private String sub_action;
    private int performedBy;
    private String justification;

    public Action() {
    }

    public String getAction() {
        return action;
    }

    public void setAction(String val) {
        this.action = val;
    }

    public String getJustification() {
        return justification;
    }

    public void setJustification(String val) {
        this.justification = val;
    }

    public int getPerformedBy() {
        return performedBy;
    }

    public void setPerformedBy(int val) {
        this.performedBy = val;
    }

    /**
     * @return the sub_actionId
     */
    public String getSub_action() {
        return sub_action;
    }

    /**
     * @param sub_actionId the sub_actionId to set
     */
    public void setSub_action(String sub_action) {
        this.sub_action = sub_action;
    }
}
