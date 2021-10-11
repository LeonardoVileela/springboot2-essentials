package academy.devdojo.springboot2.service;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.exception.BadRequestException;
import academy.devdojo.springboot2.mapper.AnimeMapper;
import academy.devdojo.springboot2.repository.AnimeRepository;
import academy.devdojo.springboot2.requests.AnimePostRequestBody;
import academy.devdojo.springboot2.requests.AnimePutRequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class AnimeService {

    @Autowired
    private AnimeRepository animeRepository;

    @Autowired
    private AnimeMapper animeMapper;

    // Page e Pageable é pra fazer paginação
    public Page<Anime> animeAll(Pageable pageable) {
        return animeRepository.findAll(pageable);
    }

    public List<Anime> findByName(String name) {
        return animeRepository.findByNameIgnoreCaseContaining(name);
    }

    public Anime findByIdOrThrowBadRequestException(Long id) {
        return animeRepository.findById(id).orElseThrow(
                () -> new BadRequestException("Anime not found")
        );
    }

    // sempre colocar Transactional(rollbackFor = Exception.class) em operações de mudança de status do bd
    // isso evita que os dados sejam mudados no banco de dados se ocorre um exception
    @Transactional(rollbackFor = Exception.class)
    public Anime save(AnimePostRequestBody animePostRequestBody) {
        // faço o mapeamento apenas com essa linha AnimeMapper.INSTANCE.toAnime(animePostRequestBody)
        return animeRepository.save(animeMapper.toAnime(animePostRequestBody));
    }

    public void delete(Long id) {
        animeRepository.delete(findByIdOrThrowBadRequestException(id));
    }

    @Transactional(rollbackFor = Exception.class)
    public void replace(AnimePutRequestBody animePutRequestBody) {

        //essas variaveis auxiliares são necessarias, pq é necessario que eu tenha
        //certeza que esse id vem do banco

        Anime animeSaved = findByIdOrThrowBadRequestException(animePutRequestBody.getId());
        Anime anime = animeMapper.toAnime(animePutRequestBody);
        anime.setId(animeSaved.getId());
        animeRepository.save(anime);

    }
}
