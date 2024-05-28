package nz.ac.auckland.se281;

public class FloralService extends ServiceAdder {
  private Types.FloralType floralType;

  public FloralService(String bookingReference) {
    super(bookingReference);
  }

  public Types.FloralType getFloralType() {
    return this.floralType;
  }

  @Override
  // This method is not implemented
  public void addService(
      Types.CateringType cateringType, Types.FloralType floralType, boolean isMusic) {}

  // Add service for floral
  public void addService(Types.FloralType floralType) {
    this.floralType = floralType;
    this.addServiceSuccessful();
  }

  @Override
  public void addServiceSuccessful() {
    MessageCli.ADD_SERVICE_SUCCESSFUL.printMessage(
        ("Floral (" + floralType.getName() + ")"), this.getBookingReference());
  }
}
