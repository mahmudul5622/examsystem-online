package osa.ora.beans;

import java.io.Serializable;
/*
 * @author Osama Oransa
 */
public class UserProfile implements Serializable {
    private int id;
    private String name;
    private String voucher;
    private String email;
    private String resultsEmail;
    private int levelId;
    private int roleId;
    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the loginName
     */
    public String getName() {
        return name;
    }

    /**
     * @param loginName the loginName to set
     */
    public void setName(String loginName) {
        this.name = loginName;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the voucher
     */
    public String getVoucher() {
        return voucher;
    }

    /**
     * @param voucher the voucher to set
     */
    public void setVoucher(String voucher) {
        this.voucher = voucher;
    }

    /**
     * @return the resultsEmail
     */
    public String getResultsEmail() {
        return resultsEmail;
    }

    /**
     * @param resultsEmail the resultsEmail to set
     */
    public void setResultsEmail(String resultsEmail) {
        this.resultsEmail = resultsEmail;
    }

    /**
     * @return the level
     */
    public int getLevelId() {
        return levelId;
    }

    /**
     * @param level the level to set
     */
    public void setLevelId(int levelId) {
        this.levelId = levelId;
    }

    /**
     * @return the roleId
     */
    public int getRoleId() {
        return roleId;
    }

    /**
     * @param roleId the roleId to set
     */
    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

}
