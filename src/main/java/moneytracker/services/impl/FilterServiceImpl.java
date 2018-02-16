package moneytracker.services.impl;

import moneytracker.exceptions.NotFoundException;
import moneytracker.model.Filter;
import moneytracker.model.ApplicationUser;
import moneytracker.repositories.FilterRepository;
import moneytracker.repositories.TagRepository;
import moneytracker.services.FilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FilterServiceImpl implements FilterService {

    @Autowired
    private FilterRepository filterRepository;

    @Autowired
    private TagRepository tagRepository;

    @Override
    public List<Filter> list(ApplicationUser owner) {
        List<Filter> filters = filterRepository.list(owner);
        filters.forEach(filter -> {
            filter.setTags(tagRepository.list(owner, filter));
        });

        return filters;
    }

    @Override
    public Filter get(ApplicationUser owner, Long id) throws NotFoundException {
        try {
            Filter filter = filterRepository.get(owner, id);
            filter.setTags(tagRepository.list(owner, filter));

            return filter;
        } catch (DataAccessException e) {
            throw new NotFoundException(String.format("Filter [id:%d] not found", id));
        }
    }

    @Override
    @Transactional
    public void save(Filter filter) {
        filterRepository.save(filter);
    }

    @Override
    @Transactional
    public void remove(Filter filter) {
        filterRepository.remove(filter);
    }

}
