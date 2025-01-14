package by.itclass.utils;

import by.itclass.exception.CompetitionException;
import by.itclass.model.Animal;
import by.itclass.model.Cat;
import by.itclass.model.Dog;
import by.itclass.model.Genus;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AnimalFactory {

    private static final String CHIP_REGEX = "(?=\\d{15}\\b)(?:112|643)09(?:81|56)\\d{8}";
    public static final String EMAIL_REGEX = "[a-zA-Z0-9^$|-]+(?:\\.[a-zA-Z0-9]+)*@[a-zA-Z0-9]+(?:\\.[a-zA-Z0-9]+)*$";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-y");

    public static Animal getInstance(String textString) throws CompetitionException {
        var lexems = textString.split("[,;]");
        try {
            var chipNumber = Long.parseLong(validateByRegex(lexems[0], CHIP_REGEX));
            var name = validateByEmpty(lexems[2]);
            var birthDate = LocalDate.parse(lexems[3], FORMATTER);
            var breed = validateByEmpty(lexems[4]);
            var email = validateByRegex(lexems[5], EMAIL_REGEX);
            return lexems[1].equalsIgnoreCase("cat")
                    ? new Cat(chipNumber, Genus.of(lexems[1]), name, birthDate, breed, email)
                    : new Dog(chipNumber, Genus.DOG, name, birthDate, breed, email);
        } catch (IllegalStateException e) {
            throw new CompetitionException(e, textString);
        }
    }

    private static String validateByRegex(String value, String regex) {
        if (value.matches(regex)) {
            return value;
        }
        throw new IllegalStateException("Chip number or email is invalid");
    }

    private static String validateByEmpty(String value) {
        if (!value.isEmpty()) {
            return value;
        }
        throw new IllegalStateException("Name or breed is empty");
    }
}
