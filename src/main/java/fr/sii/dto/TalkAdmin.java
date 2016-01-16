package fr.sii.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Talk DTO for admin view
 */
public class TalkAdmin extends TalkUser {

    private int userId;

    private boolean reviewed;

    private BigDecimal mean;
    private List<String> voteUsersEmail;

    public void setMean(Double mean) {
        if (mean == null) return;

        if (mean == 0) {
            this.mean = BigDecimal.ZERO;
        } else {
            this.mean = new BigDecimal(mean).setScale(2, RoundingMode.HALF_EVEN);
        }
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean isReviewed() {
        return reviewed;
    }

    public void setReviewed(boolean reviewed) {
        this.reviewed = reviewed;
    }

    public BigDecimal getMean() {
        return mean;
    }

    public void setMean(BigDecimal mean) {
        this.mean = mean;
    }

    public List<String> getVoteUsersEmail() {
        return voteUsersEmail;
    }

    public void setVoteUsersEmail(List<String> voteUsersEmail) {
        this.voteUsersEmail = voteUsersEmail;
    }
}
