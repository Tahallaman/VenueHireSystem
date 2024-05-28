package nz.ac.auckland.se281;

public abstract class ServiceAdder {

  private String bookingReference;

  public ServiceAdder(String bookingReference) {

    this.bookingReference = bookingReference;
  }

  // Get the booking
  public String getBookingReference() {

    return bookingReference;
  }

  // Abstract methods to be implemented by services extending this class
  public abstract void addService(
      Types.CateringType cateringType, Types.FloralType floralType, boolean isMusic);

  public abstract void addServiceSuccessful();

  // Static method to print message when service is not added
  static void serviceNotAdded(String bookingReference, String serviceType) {

    MessageCli.SERVICE_NOT_ADDED_BOOKING_NOT_FOUND.printMessage(serviceType, bookingReference);
  }
}
