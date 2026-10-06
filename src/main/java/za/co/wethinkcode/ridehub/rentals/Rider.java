package za.co.wethinkcode.ridehub.rentals;

public class Rider {

    private final String name;
    private final boolean verified;
    private final boolean banned;
    private final double creditBalance;

    public Rider(String name, boolean verified, boolean banned, double creditBalance) {
        this.name = name;
        this.verified = verified;
        this.banned = banned;
        this.creditBalance = creditBalance;
    }

    public String getName() {
        return name;
    }

    public boolean isVerified() {
        return verified;
    }

    public boolean isBanned() {
        return banned;
    }

    public double getCreditBalance() {
        return creditBalance;
    }
}
