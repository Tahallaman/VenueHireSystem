package nz.ac.auckland.se281;

public class Booking {
  private String venueCode;
  private Venue venue;
  private String date;
  private String dateBooked;
  private String email;
  private String bookingReference;
  private int numAttending;
  private CateringService cateringService;
  private FloralService floralService;
  private MusicService musicService;

  public Booking(
      String venueCode,
      String date,
      String email,
      String bookingReference,
      int numAttending,
      Venue venue,
      String dateBooked) {
    this.venueCode = venueCode;
    this.venue = venue;
    this.date = date;
    this.dateBooked = dateBooked;
    this.email = email;
    this.bookingReference = bookingReference;
    this.numAttending = numAttending;
    this.cateringService = new CateringService(bookingReference);
    this.floralService = new FloralService(bookingReference);
    this.musicService = new MusicService(bookingReference);
  }

  // These are the getters for the Booking class
  public String getVenueCode() {
    return this.venueCode;
  }

  public Venue getVenue() {
    return this.venue;
  }

  public String getDate() {
    return this.date;
  }

  public String getDateBooked() {
    return this.dateBooked;
  }

  public String getEmail() {
    return this.email;
  }

  public String getBookingReference() {
    return this.bookingReference;
  }

  public int numAttending() {
    return this.numAttending;
  }

  // These return the sub-services instances of the booking
  public MusicService getMusic() {
    return this.musicService;
  }

  public FloralService getFloral() {
    return this.floralService;
  }

  public CateringService getCatering() {
    return this.cateringService;
  }
}
