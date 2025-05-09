package uz.muxtoriyat.service;

import java.util.List;
import uz.muxtoriyat.service.dto.view.SourceViewDTO;

public interface SourceViewService {
    List<SourceViewDTO> fillReactions(List<SourceViewDTO> sourceViews);
}
