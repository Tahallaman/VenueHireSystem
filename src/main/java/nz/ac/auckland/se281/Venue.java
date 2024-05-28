package nz.ac.auckland.se281;

public class Venue {
  // Instance variables for the Venue class
  private String name;
  private String code;
  private int capacity;
  private int hireFee;
  private String nextAvaliableDate;

  // Constructor for the Venue class
  public Venue(String name, String code, int capacity, int hireFee) {
    this.name = name;
    this.code = code;
    this.capacity = capacity;
    this.hireFee = hireFee;
    this.nextAvaliableDate = "00/00/0000";
  }

  // Get the name of the venue
  public String getName() {
    return this.name;
  }

  // Get the name of the venue
  public String getCode() {
    return this.code;
  }

  // Get the capacity of the venue
  public int getCapacity() {
    return this.capacity;
  }

  // Get the hire fee for the venue
  public int getHireFee() {
    return this.hireFee;
  }

  // Get the next avaliable date for venue
  public String getNextAvaliableDate() {
    return this.nextAvaliableDate;
  }

  // Update Next avaliable date for venue
  public void updateNextAvaliableDate(String date) {
    this.nextAvaliableDate = date;
  }
}
