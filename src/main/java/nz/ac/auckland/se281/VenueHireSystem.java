package nz.ac.auckland.se281;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import nz.ac.auckland.se281.Types.CateringType;
import nz.ac.auckland.se281.Types.FloralType;

public class VenueHireSystem {
  private List<Venue> venues = new ArrayList<>();
  private List<Booking> bookings = new ArrayList<>();
  private String systemDate = "not set";
  private Venue venueBooking;
  private Venue venuePrint;

  public VenueHireSystem() {}

  public void printVenues() {
    // No venues in the system
    if (venues.size() <= 0) {
      MessageCli.NO_VENUES.printMessage();
      return;
    }
    if (venues.size() == 1) {
      // 1 Venue in the system
      MessageCli.NUMBER_VENUES.printMessage("is", "one", "");
    } else if (venues.size() < 10) {
      // More than 1, less than 10 Venues in the system
      String[] numbers = {"two", "three", "four", "five", "six", "seven", "eight", "nine"};
      MessageCli.NUMBER_VENUES.printMessage("are", numbers[venues.size() - 2], "s");
    } else if (venues.size() >= 10) {
      // More than 10 Venues in the system
      MessageCli.NUMBER_VENUES.printMessage("are", String.valueOf(venues.size()), "s");
    }
    // If SystemDate hasn't been set
    String nextAvaliableString = "";
    Boolean sysDateSet = !systemDate.contentEquals("not set");

    for (Venue venue : venues) {
      // Loop through venues and output venue_entry
      if (sysDateSet) {
        nextAvaliableString = venue.getNextAvaliableDate();
      }
      MessageCli.VENUE_ENTRY.printMessage(
          venue.getName(),
          venue.getCode(),
          String.valueOf(venue.getCapacity()),
          String.valueOf(venue.getHireFee()),
          nextAvaliableString);
    }
  }

  public void createVenue(
      String venueName, String venueCode, String capacityInput, String hireFeeInput) {
    int capacity;
    int hireFee;
    // Checks if venueName is valid and not blank
    if (venueName.trim().isEmpty()) {
      MessageCli.VENUE_NOT_CREATED_EMPTY_NAME.printMessage();
      return;
    }
    // Watch for valid inputs for capacity and check if positive
    try {
      capacity = Integer.parseInt(capacityInput);
      if (capacity <= 0) {
        MessageCli.VENUE_NOT_CREATED_INVALID_NUMBER.printMessage("capacity", " positive");
        return;
      }
    } catch (NumberFormatException e) {
      MessageCli.VENUE_NOT_CREATED_INVALID_NUMBER.printMessage("capacity", "");
      return;
    }
    // Watch for valid inputs for hireFee and check if positive
    try {
      hireFee = Integer.parseInt(hireFeeInput);
      if (hireFee <= 0) {
        MessageCli.VENUE_NOT_CREATED_INVALID_NUMBER.printMessage("hire fee", " positive");
        return;
      }
    } catch (NumberFormatException e) {
      MessageCli.VENUE_NOT_CREATED_INVALID_NUMBER.printMessage("hire fee", "");
      return;
    }
    // Check if the venue code is used
    for (Venue venue : venues) {
      if (venue.getCode().equals(venueCode)) {
        MessageCli.VENUE_NOT_CREATED_CODE_EXISTS.printMessage(venue.getCode(), venue.getName());
        return;
      }
    }
    // If all checks pass, create the venue instance
    venues.add(new Venue(venueName, venueCode, capacity, hireFee));
    MessageCli.VENUE_SUCCESSFULLY_CREATED.printMessage(venueName, venueCode);
  }

  public void setSystemDate(String dateInput) {
    systemDate = dateInput;
    MessageCli.DATE_SET.printMessage(dateInput);

    // If there is one or more than 1 venue, Check venue avaliability for after system date, if not
    // update to systemDate
    if (!venues.isEmpty()) {
      for (Venue venue : venues) {
        if (date1B4date2(venue.getNextAvaliableDate(), dateInput)) {
          venue.updateNextAvaliableDate(dateInput);
        }
      }
    }
  }

  public void printSystemDate() {
    MessageCli.CURRENT_DATE.printMessage(systemDate);
  }

  public void makeBooking(String[] options) {
    // System Date Must Be Set
    if (systemDate.equals("not set")) {
      MessageCli.BOOKING_NOT_MADE_DATE_NOT_SET.printMessage();
      return;
      // There Must Be at least one venue in the system
    }
    if (venues.size() <= 0) {
      MessageCli.BOOKING_NOT_MADE_NO_VENUES.printMessage();
      return;
    }
    // The Venue code must exist
    for (Venue venue : venues) {
      if (venue.getCode().contains(options[0])) {
        venueBooking = venue;
        break;
      }
    }
    if (venueBooking == null) {
      MessageCli.BOOKING_NOT_MADE_VENUE_NOT_FOUND.printMessage(options[0]);
      return;
    }

    // The venue must be avaliable on the specified date
    for (Booking booking : bookings) {
      if (booking.getDate().contentEquals(options[1])
          && booking.getVenueCode().equals(venueBooking.getCode())) {
        MessageCli.BOOKING_NOT_MADE_VENUE_ALREADY_BOOKED.printMessage(
            venueBooking.getName(), booking.getDate());
        return;
      }
    }

    // The booking date must not be in the past
    if (date1B4date2(options[1], systemDate)) {
      MessageCli.BOOKING_NOT_MADE_PAST_DATE.printMessage(options[1], systemDate);
      return;
    }
    // If attendance % is less than %25 capacity, make %25
    // else if attendance % is more than %100 capacity, make %100
    int attendance = Integer.valueOf(options[3]);
    if (attendance < (int) (venueBooking.getCapacity() * 0.25)) {
      attendance = (int) (venueBooking.getCapacity() * 0.25);
      MessageCli.BOOKING_ATTENDEES_ADJUSTED.printMessage(
          options[3], String.valueOf(attendance), String.valueOf(venueBooking.getCapacity()));
    } else if (attendance > venueBooking.getCapacity()) {

      attendance = venueBooking.getCapacity();
      MessageCli.BOOKING_ATTENDEES_ADJUSTED.printMessage(
          options[3], String.valueOf(attendance), String.valueOf(venueBooking.getCapacity()));
    }
    // Once Checks are complete, Create Booking instance
    String bookingReference = BookingReferenceGenerator.generateBookingReference();
    bookings.add(
        new Booking(
            options[0],
            options[1],
            options[2],
            bookingReference,
            attendance,
            venueBooking,
            systemDate));
    MessageCli.MAKE_BOOKING_SUCCESSFUL.printMessage(
        bookingReference, venueBooking.getName(), options[1], String.valueOf(attendance));

    // Sets venue availability accordingly

    // if the venue's next avaliable date is the same as the booking, look for the next avaliable
    // date
    if (venueBooking.getNextAvaliableDate().contentEquals(options[1])) {
      String checkDate = options[1];
      Boolean dateNotAvailable = true;
      while (dateNotAvailable) {
        dateNotAvailable = false;
        for (Booking booking : bookings) {
          if (booking.getDate().equals(checkDate)
              && booking.getVenueCode().equals(venueBooking.getCode())) {
            dateNotAvailable =
                true; // The date is not available if there's a booking for the same venue
            break;
          }
        }
        if (dateNotAvailable) {
          checkDate = addOneDay(checkDate); // If the date is not available, check the next day
        }
      }
      venueBooking.updateNextAvaliableDate(checkDate);
    }
  }

  public void printBookings(String venueCode) {
    // The Venue code must exist else return
    for (Venue venue : venues) {
      if (venue.getCode().contains(venueCode)) {
        venuePrint = venue;
        break;
      }
    }
    if (venuePrint == null) {
      MessageCli.PRINT_BOOKINGS_VENUE_NOT_FOUND.printMessage(venueCode);
      return;
    }
    // Print Bookings
    Boolean isBooking = false;
    MessageCli.PRINT_BOOKINGS_HEADER.printMessage(venuePrint.getName());
    for (Booking booking : bookings) {
      if (booking.getVenueCode().equals(venuePrint.getCode())) {
        MessageCli.PRINT_BOOKINGS_ENTRY.printMessage(
            booking.getBookingReference(), booking.getDate());
        isBooking = true;
      }
    }
    if (!isBooking) {
      MessageCli.PRINT_BOOKINGS_NONE.printMessage(venuePrint.getName());
    }
  }

  public void addCateringService(String bookingReference, CateringType cateringType) {
    Booking cateringBooking = null;
    // Check if booking reference exists
    boolean bookingReferenceExists = false;
    for (Booking booking : bookings) {
      if (booking.getBookingReference().equals(bookingReference)) {
        bookingReferenceExists = true;
        cateringBooking = booking;
        break;
      }
    }
    if (!bookingReferenceExists) {
      ServiceAdder.serviceNotAdded(bookingReference, "Catering");
      ;
      return;
    }

    // Add catering type to booking
    cateringBooking.getCatering().addService(cateringType);
  }

  public void addServiceMusic(String bookingReference) {
    Booking musicBooking = null;
    // Check if booking reference exists
    boolean bookingReferenceExists = false;
    for (Booking booking : bookings) {
      if (booking.getBookingReference().equals(bookingReference)) {
        bookingReferenceExists = true;
        musicBooking = booking;
        break;
      }
    }
    if (!bookingReferenceExists) {
      ServiceAdder.serviceNotAdded(bookingReference, "Music");
      ;
      return;
    }

    // Add music to booking
    musicBooking.getMusic().addService(true);
  }

  public void addServiceFloral(String bookingReference, FloralType floralType) {
    Booking floralBooking = null;
    // Check if booking reference exists
    boolean bookingReferenceExists = false;
    for (Booking booking : bookings) {
      if (booking.getBookingReference().equals(bookingReference)) {
        bookingReferenceExists = true;
        floralBooking = booking;
        break;
      }
    }
    if (!bookingReferenceExists) {
      ServiceAdder.serviceNotAdded(bookingReference, "Floral");
      ;
      return;
    }

    // Add floral type to booking
    floralBooking.getFloral().addService(floralType);
  }

  public void viewInvoice(String bookingReference) {
    Booking invoiceBooking = null;
    // Check if booking reference exists
    boolean bookingReferenceExists = false;
    for (Booking booking : bookings) {
      if (booking.getBookingReference().equals(bookingReference)) {
        bookingReferenceExists = true;
        invoiceBooking = booking;
        break;
      }
    }
    if (!bookingReferenceExists) {
      MessageCli.VIEW_INVOICE_BOOKING_NOT_FOUND.printMessage(bookingReference);
      return;
    }
    MessageCli.INVOICE_CONTENT_TOP_HALF.printMessage(
        bookingReference,
        invoiceBooking.getEmail(),
        invoiceBooking.getDateBooked(),
        invoiceBooking.getDate(),
        Integer.toString(invoiceBooking.numAttending()),
        invoiceBooking.getVenue().getName());

    // Start calculating total fee starting with the venue hirage and add as you go on
    int totalFee = invoiceBooking.getVenue().getHireFee();
    MessageCli.INVOICE_CONTENT_VENUE_FEE.printMessage(Integer.toString(totalFee));

    // If catering has been added
    if (invoiceBooking.getCatering().getCateringType() != null) {
      int cateringCost =
          invoiceBooking.getCatering().getCateringType().getCostPerPerson()
              * invoiceBooking.numAttending();
      MessageCli.INVOICE_CONTENT_CATERING_ENTRY.printMessage(
          invoiceBooking.getCatering().getCateringType().getName(), Integer.toString(cateringCost));
      totalFee = totalFee + cateringCost;
    }
    // If music has been added
    if (invoiceBooking.getMusic().isMusic()) {
      MessageCli.INVOICE_CONTENT_MUSIC_ENTRY.printMessage("500");
      totalFee = totalFee + 500;
    }

    // If Floral services have been added
    if (invoiceBooking.getFloral().getFloralType() != null) {
      int floralCost = invoiceBooking.getFloral().getFloralType().getCost();
      MessageCli.INVOICE_CONTENT_FLORAL_ENTRY.printMessage(
          invoiceBooking.getFloral().getFloralType().getName(), Integer.toString(floralCost));
      totalFee = totalFee + floralCost;
    }

    MessageCli.INVOICE_CONTENT_BOTTOM_HALF.printMessage(Integer.toString(totalFee));
  }

  // Helper Methods
  private boolean date1B4date2(String fullDate1, String fullDate2) {
    // This method will check whether date1 is before date 2 and return a boolean
    String[] date1 = fullDate1.split("/");
    String[] date2 = fullDate2.split("/");

    int[] date1Int = {
      Integer.valueOf(date1[0]), Integer.valueOf(date1[1]), Integer.valueOf(date1[2])
    };
    int[] date2Int = {
      Integer.valueOf(date2[0]), Integer.valueOf(date2[1]), Integer.valueOf(date2[2])
    };
    // Check on a year then month then day basis
    if (date1Int[2] < date2Int[2]
        || (date1Int[2] == date2Int[2] && date1Int[1] < date2Int[1])
        || (date1Int[2] == date2Int[2]
            && date1Int[1] == date2Int[1]
            && date1Int[0] < date2Int[0])) {
      return true;
    }
    return false;
  }

  private String addOneDay(String date) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    LocalDate localDate = LocalDate.parse(date, formatter);
    LocalDate nextDay = localDate.plusDays(1);
    return nextDay.format(formatter);
  }
}
