package osa.ora.beans;

import java.io.Serializable;
/*
 * @author Osama Oransa
 */
public class Options implements Serializable {
    private int optionId;
    private String optionValue;

    /**
     * @return the optionId
     */
    public int getOptionId() {
        return optionId;
    }

    /**
     * @param optionId the optionId to set
     */
    public void setOptionId(int optionId) {
        this.optionId = optionId;
    }

    /**
     * @return the optionValue
     */
    public String getOptionValue() {
        return optionValue;
    }

    /**
     * @param optionValue the optionValue to set
     */
    public void setOptionValue(String optionValue) {
        this.optionValue = optionValue;
    }
}
