package nz.ac.auckland.se281;

public class MusicService extends ServiceAdder {
  private boolean isMusic;

  public MusicService(String bookingReference) {
    super(bookingReference);
  }

  public boolean isMusic() {
    return this.isMusic;
  }

  @Override
  // This method is not implemented
  public void addService(
      Types.CateringType cateringType, Types.FloralType floralType, boolean isMusic) {}

  // Add service for music
  public void addService(boolean isMusic) {
    this.isMusic = isMusic;
    this.addServiceSuccessful();
  }

  @Override
  public void addServiceSuccessful() {
    MessageCli.ADD_SERVICE_SUCCESSFUL.printMessage("Music", this.getBookingReference());
  }
}
