package fr.sii.service.admin.comment;

import fr.sii.domain.admin.comment.AdminComment;
import fr.sii.domain.admin.user.AdminUser;
import fr.sii.domain.exception.NotFoundException;
import fr.sii.repository.admin.comment.AdminCommentRepository;
import fr.sii.service.admin.user.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by tmaugin on 29/04/2015.
 */
@Service
public class AdminCommentService {

    @Autowired
    private AdminCommentRepository adminCommentRepository;

    @Autowired
    private AdminUserService adminUserService;

    public List<AdminComment> matchUsers(List<AdminComment> rs)
    {
        List<AdminComment> nrs = new ArrayList<>();
        for (AdminComment r : rs)
        {
            try {
                AdminUser u = adminUserService.findOne(r.getUserId());
                r.setUser(u);
                nrs.add(r);
            } catch (NotFoundException e) {
                //e.printStackTrace();
            }
        }
        return nrs;
    }
    public AdminComment matchUser(List<AdminComment> rs) throws NotFoundException {
        if(rs.size() > 0)
        {
            AdminComment r = rs.get(0);
            try {
                AdminUser u = adminUserService.findOne(r.getUserId());
                r.setUser(u);
            } catch (NotFoundException e) {
                //e.printStackTrace();
            }
            return r;
        }
        throw new NotFoundException("Comment not found");
    }

    public AdminComment matchUser(AdminComment r){
        try {
            AdminUser u = adminUserService.findOne(r.getUserId());
            r.setUser(u);
        } catch (NotFoundException e) {
            //e.printStackTrace();
        }
        return r;
    }

    public List<AdminComment> findAll()
    {
        return matchUsers(adminCommentRepository.findAll());
    }

    public void deleteAll()
    {
        adminCommentRepository.deleteAll();
    }

    public AdminComment save(AdminComment r) {
        if(r.getAdded() == null)
        {
            r.setAdded(new Date());
        }
        return matchUser(adminCommentRepository.save(r));
    }

    public AdminComment put(Long id,AdminComment r) throws NotFoundException {
        if(r.getAdded() == null)
        {
            r.setAdded(new Date());
        }
        delete(id);
        r.setEntityId(id);
        return matchUser(adminCommentRepository.save(r));
    }

    public void delete(Long id) throws NotFoundException {
        findOne(id);
        adminCommentRepository._delete(id);
    }

    public AdminComment findOne(Long id) throws NotFoundException {
        return matchUser(adminCommentRepository.findByEntityId(id));
    }

    public List<AdminComment> findByUserId(Long id)
    {
        return matchUsers(adminCommentRepository.findByUserId(id));
    }

    public List<AdminComment> findByRowId(Long rowId)
    {
        return matchUsers(adminCommentRepository.findByRowIdOrderByAddedAsc(rowId));
    }
}