package com.example.schedule.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseRepository<T,ID> extends JpaRepository<T,ID> {
    default T findOrThrow(ID id, String entityName) {
        return findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, entityName.toLowerCase() + "Id : " + "해당 " + entityName.toLowerCase() +  " 이(가) 존재하지 않습니다"));
    }
}

//공통으로 사용하는 findOrThrow 함수를 BaseRepository 에 정의하여 코드 반복을 줄였습니다