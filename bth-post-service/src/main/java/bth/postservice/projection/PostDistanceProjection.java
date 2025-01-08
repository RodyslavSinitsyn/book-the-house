package bth.postservice.projection;

import bth.common.dto.BookingStatus;

import java.time.LocalDate;

public interface PostDistanceProjection {
    String getId();
    String getUsername();
    String getFriendlyName();
    String getTitle();
    String getDescription();
    String getImageUrl();
    Integer getPrice();
    String getCity();
    String getCountry();
    String getState();
    BookingStatus getStatus();
    LocalDate getFromDate();
    LocalDate getToDate();
}
