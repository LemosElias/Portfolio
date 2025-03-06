package ResApiMusic.Musica.resource;

import ResApiMusic.Musica.Model.Playlist;
import ResApiMusic.Musica.Model.User;
import ResApiMusic.Musica.Service.AuthorizationService;
import ResApiMusic.Musica.Service.PlaylistService;
import ResApiMusic.Musica.dto.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/playlist")
public class PlaylistResours {
    @Autowired
    private AuthorizationService authorizationService;
    @Autowired
    private PlaylistService service;

    @GetMapping(produces = "application/json")//este metodo utiliza el servicio para obtener las playlist , convierte playlist en playlistsRequestdto calcula la cantidad y retorna //
    public ResponseEntity<?>getAllPlaylist(@RequestHeader(name = "Authorization") String token){
        try {
            authorizationService.authorize(token);
            ModelMapper modelMapper=new ModelMapper();
            List<Playlist> playlists = service.getallPlaylist();
            List<PlaylistResquestDto> playlistResponseDTOs = playlists.stream()
                    .map(playlist -> {
                        PlaylistResquestDto dto = modelMapper.map(playlist, PlaylistResquestDto.class);
                        dto.setSongCount(playlist.getSongs().size());
                        return dto;
                    })
                    .collect(Collectors.toList());
            return ResponseEntity.ok(playlistResponseDTOs);
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
    }
    @GetMapping("/{id}") //no es que no me guste es que ta como rarooo//
    //metodo que utiliza el servicio para obtener el detalle de la playlist//
    public ResponseEntity<?>getPlaylistById(@PathVariable Long id){
        Optional<Playlist> playlistOptional=service.getPlaylistById(id); //pbtengo el valor de el optional//
        ModelMapper modelMapper=new ModelMapper();
        if (playlistOptional.isPresent()){ //compruebo si el optional tiene algun valor//
            Playlist playlist=playlistOptional.get();
            PlaylistDetailsResponseDTO playlistDetailsResponseDTO=modelMapper.map(playlist,PlaylistDetailsResponseDTO.class);
            List<SongResponseDTO> songResponseDTOS = playlist.getSongs().stream()
                    .map(song -> modelMapper.map(song, SongResponseDTO.class))
                    .collect(Collectors.toList());
            playlistDetailsResponseDTO.setSongs(songResponseDTOS);
            playlistDetailsResponseDTO.setSongCount(songResponseDTOS.size());
            return ResponseEntity.ok(playlistDetailsResponseDTO);
        } else {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);

        }
    }
    @PostMapping("/playlists")
    public ResponseEntity<?>createPlaylist( @RequestBody CreatePlaylistRequestDTO createPlaylistRequestDTO) {
            ModelMapper modelMapper= new ModelMapper();
            //llamo al servicio para crear la Playlist//
           Playlist playlist=service.createPlaylist(createPlaylistRequestDTO.getName());
           //uso modelmaper para mapear//
           PlaylistDetailsResponseDTO playlistDetailsResponseDTO= modelMapper.map(playlist,PlaylistDetailsResponseDTO.class);
           //retorun un 201 con el DTO creado //
           return ResponseEntity.status(201).body(playlistDetailsResponseDTO);
    }
    @PutMapping("/{id}")
    public ResponseEntity<?>updatePlaylist(@RequestHeader(name = "Authorization") String token, @PathVariable Long id, @RequestBody UpdatePlaylistResponseDTO updatePlaylistResponseDTO){
        try {
            User user = authorizationService.authorize(token);
            service.updatePlaylist(id,updatePlaylistResponseDTO.getName());
            return ResponseEntity.status(200).build();
        }catch (Exception e){
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?>DeletePlaylist(@RequestHeader("Authorization") String token, @PathVariable Long id){
        try {
            User user = authorizationService.authorize(token); //obtengo el usuario mediante el token//
            Playlist playlist=service.getPlaylistById(id).orElseThrow(); //llamo al servicio encargado de la eliminacion de la playlist//
            service.DeletePlaylist(id,user);//le paso el id y el usuario al servicio//
            return ResponseEntity.status(201).build();
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }
    @PostMapping("/{id}/songs") //este metodo utilizara el servicio para agregar la cancion a la playlist//
    public ResponseEntity<?>addSongPlaylist(@RequestHeader(name = "Authorization") String token, @PathVariable Long id, @RequestBody AddSongToPlaylistRequestDTO addSongToPlaylistRequestDTO){
        try {
            User user = authorizationService.authorize(token);
            ModelMapper modelMapper=new ModelMapper();
            Playlist playlist=service.addSongPlaylist(id,addSongToPlaylistRequestDTO.getSongId(),user);
            AddSongPlayist addSongPlayist=modelMapper.map(playlist,AddSongPlayist.class);
            return ResponseEntity.ok(addSongPlayist);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);

        }
    }
    @DeleteMapping("/{id}/songs/{songId}")
    public ResponseEntity<?> removeSongFromPlaylist(@RequestHeader("Authorization") String token, @PathVariable Long id, @PathVariable Long songId) {
        try {
            User user = authorizationService.authorize(token); //obtengo el usuario //
            Playlist playlist= service.removeSongFomPlaylist(id,songId,user); //utilizo el servis para eliminar la cancion//
            return ResponseEntity.status(201).build();


        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }
    //metodo que vamos a tener que probar muy bien//
    @GetMapping("/playlists")
    public ResponseEntity<?>getCurrentUserPlaylists(@RequestHeader("Authorization") String token) {
        try {
            User user = authorizationService.authorize(token); //obtengo el usuario //
            ModelMapper modelMapper=new ModelMapper();
            List<Playlist> playlists= service.getPlaylistByUser(user);
            List<AddSongPlayist>playlistResponseDTOs= (List<AddSongPlayist>) playlists.stream()
                    .map(playlist -> modelMapper.map(playlist, AddSongPlayist.class))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(playlistResponseDTOs);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    }


