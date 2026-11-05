package ru.gymhub.exceptions.exceptions;

public class GymNotFoundException extends RuntimeException {
  public GymNotFoundException(Long id) {
    super("Gym not found with id: " + id);
  }
}
