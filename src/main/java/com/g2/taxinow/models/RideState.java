package com.g2.taxinow.models;

/**
 * Possible states for a ride
 */
public enum RideState {
    PENDING,
    ACCEPTED,
    COMPLETED;

    @Override
    public String toString() {
        switch (this) {
            case PENDING:
                return "Pending";
            case ACCEPTED:
                return "Accepted";
            case COMPLETED:
                return "Completed";
            default:
                return "No state";
        }
    }

}