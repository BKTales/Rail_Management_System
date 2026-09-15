package org.dei.Sprint2;

import java.util.Objects;

public class Country implements Comparable<Country> {
    private String abbreviaton;

    /**
     * Constructs a Country with abbreviation and time zone.
     *
     * @param abbreviaton the country abbreviation (e.g., "US", "UK", "FR")
     */
    public Country(String abbreviaton) {
        this.abbreviaton = abbreviaton;
    }

    public String getAbbreviaton() {
        return abbreviaton;
    }

    @Override
    public int compareTo(Country o) {
        int result = compareCountry(o);

        return result;
    }

    private int compareCountry(Country o) {
        return abbreviaton.compareTo(o.getAbbreviaton());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Country country = (Country) o;
        return Objects.equals(abbreviaton, country.abbreviaton);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(abbreviaton);
    }
    public String toString() {
        return abbreviaton;
    }
}