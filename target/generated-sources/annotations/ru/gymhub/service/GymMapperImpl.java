package ru.gymhub.service;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import ru.gymhub.dto.CreateGymRequest;
import ru.gymhub.dto.GymDto;
import ru.gymhub.entity.Gym;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-11-10T20:05:38+0000",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.13 (Termux)"
)
@Component
public class GymMapperImpl implements GymMapper {

    @Override
    public Gym createRequestToGym(CreateGymRequest dto) {
        if ( dto == null ) {
            return null;
        }

        Gym.GymBuilder gym = Gym.builder();

        gym.name( dto.getName() );
        gym.description( dto.getDescription() );
        gym.address( dto.getAddress() );
        gym.tel( dto.getTel() );
        gym.email( dto.getEmail() );

        return gym.build();
    }

    @Override
    public GymDto gymToDto(Gym gym) {
        if ( gym == null ) {
            return null;
        }

        GymDto.GymDtoBuilder gymDto = GymDto.builder();

        gymDto.id( gym.getId() );
        gymDto.name( gym.getName() );
        gymDto.description( gym.getDescription() );
        gymDto.address( gym.getAddress() );
        gymDto.tel( gym.getTel() );
        gymDto.email( gym.getEmail() );

        return gymDto.build();
    }
}
