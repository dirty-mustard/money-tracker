package moneytracker.services.impl;

import moneytracker.exceptions.NotFoundException;
import moneytracker.model.Tag;
import moneytracker.model.ApplicationUser;
import moneytracker.repositories.TagRepository;
import moneytracker.repositories.TransactionRepository;
import moneytracker.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public List<Tag> list(ApplicationUser owner) {
        return tagRepository.list(owner);
    }

    @Override
    public List<Tag> list(ApplicationUser owner, List<Long> ids) {
        return tagRepository.list(owner, ids);
    }

    @Override
    public Tag get(ApplicationUser owner, Long id) throws NotFoundException {
        try {
            return tagRepository.get(owner, id);
        } catch (DataAccessException e) {
            throw new NotFoundException(String.format("Tag [id:%d] not found", id));
        }
    }

    @Override
    @Transactional
    public void save(Tag tag) {
        tagRepository.save(tag);
    }

    @Override
    @Transactional
    public void remove(Tag tag) {
        tagRepository.remove(tag);
        transactionRepository.removeFromTransactions(tag);
    }

}
