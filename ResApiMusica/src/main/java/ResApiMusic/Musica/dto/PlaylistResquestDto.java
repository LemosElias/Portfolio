package ResApiMusic.Musica.dto;

public class PlaylistResquestDto { //dto creado para obtener la respuesta pedida//
    private String name;
    private int SongCount;

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
}
