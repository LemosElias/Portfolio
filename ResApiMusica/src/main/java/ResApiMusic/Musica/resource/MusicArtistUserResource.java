package ResApiMusic.Musica.resource;

import ResApiMusic.Musica.Model.MusicArtistUser;
import ResApiMusic.Musica.Model.User;
import ResApiMusic.Musica.Service.AuthenticathionService;
import ResApiMusic.Musica.Service.UserService;
import ResApiMusic.Musica.dto.AuthenticationRequestDTO;
import ResApiMusic.Musica.dto.CreateUserRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.modelmapper.ModelMapper;



@RestController
@RequestMapping("/Artist")

public class MusicArtistUserResource {
    @Autowired
    private UserService service;
    @Autowired
    private AuthenticathionService authenticathionService;
    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateUserRequestDTO userDTO){
        ModelMapper modelMapper = new ModelMapper();
        MusicArtistUser user =modelMapper.map(userDTO,MusicArtistUser.class);
        try{
            service.createArtist(user);
            return new ResponseEntity<>(null, HttpStatus.CREATED);

        }catch(Exception e){
            return new ResponseEntity<>(null,HttpStatus.CONFLICT);
        }

    }
    @PostMapping(path="/auth",produces = "application/json")
    public ResponseEntity<?> authentication (@RequestBody AuthenticationRequestDTO authenticationRequestDTO){
        ModelMapper modelMapper=new ModelMapper();
        MusicArtistUser artistUser =modelMapper.map(authenticationRequestDTO,MusicArtistUser.class);
        try{
            String token = authenticathionService.authenticate(artistUser);
            MultiValueMap<String,String>multiValueMap=new LinkedMultiValueMap<>();
            multiValueMap.add("token", token);
            return new ResponseEntity<>(token,null,HttpStatus.OK);

        }catch (Exception e){
            return new ResponseEntity<>(null,HttpStatus.UNAUTHORIZED);
        }




    }






}
