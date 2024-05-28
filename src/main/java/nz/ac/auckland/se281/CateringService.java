package nz.ac.auckland.se281;

public class CateringService extends ServiceAdder {
  private Types.CateringType cateringType;

  public CateringService(String bookingReference) {
    super(bookingReference);
  }

  public Types.CateringType getCateringType() {
    return this.cateringType;
  }

  @Override
  // This method is not implemented
  public void addService(
      Types.CateringType cateringType, Types.FloralType floralType, boolean isMusic) {}

  // Add service for catering
  public void addService(Types.CateringType cateringType) {
    this.cateringType = cateringType;
    this.addServiceSuccessful();
  }

  @Override
  public void addServiceSuccessful() {
    MessageCli.ADD_SERVICE_SUCCESSFUL.printMessage(
        ("Catering (" + cateringType.getName() + ")"), this.getBookingReference());
  }
}
