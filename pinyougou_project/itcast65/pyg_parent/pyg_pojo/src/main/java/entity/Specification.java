package entity;

import com.pinyougou.pojo.TbSpecification;
import com.pinyougou.pojo.TbSpecificationOption;

import java.io.Serializable;
import java.util.List;

public class Specification implements Serializable {

    private TbSpecification specification;
    private List<TbSpecificationOption> specOptionList;


    public TbSpecification getSpecification() {
        return specification;
    }

    public void setSpecification(TbSpecification specification) {
        this.specification = specification;
    }

    public List<TbSpecificationOption> getSpecOptionList() {
        return specOptionList;
    }

    public void setSpecOptionList(List<TbSpecificationOption> specOptionList) {
        this.specOptionList = specOptionList;
    }
}
