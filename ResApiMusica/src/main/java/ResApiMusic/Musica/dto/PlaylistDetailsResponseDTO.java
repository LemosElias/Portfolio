package ResApiMusic.Musica.dto;

import ResApiMusic.Musica.Model.Song;

import java.util.List;

public class PlaylistDetailsResponseDTO {
    private String name;
    private  int SongCount;
    private List<SongResponseDTO>songs;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSongCount() {
        return SongCount;
    }

    public void setSongCount(int songCount) {
        SongCount = songCount;
    }

    public List<SongResponseDTO> getSongs() {
        return songs;
    }

    public void setSongs(List<SongResponseDTO> songs) {
        this.songs = songs;
    }
}
