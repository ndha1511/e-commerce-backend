import React, { useEffect, useState } from "react";
import { Button, Modal, OverlayTrigger, Tooltip, TooltipProps } from "react-bootstrap";
import { useCreateCategoryMutation } from "../../../services/category.service";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faImage, faPlus } from "@fortawesome/free-solid-svg-icons";
import 'react-toastify/dist/ReactToastify.css';
import { useDispatch } from "react-redux";
import { setNotify } from "../../../rtk/slice/notify-slice";
import ModalLoading from "../../../components/loading/ModalLoading";

interface Props {
  show: boolean;
  handleClose: () => void;
  refetch?: () => void;
  parentId?: string;
}

const CreateCategoryModal = ({ show, handleClose, refetch, parentId }: Props) => {
  const [file, setFile] = React.useState<File>();
  const [url, setUrl] = React.useState('');
  const [categoryName, setCategoryName] = React.useState('');
  const [isValid, setIsValid] = useState(true);
  const [trigger, { isLoading }] = useCreateCategoryMutation();
  const dispatch = useDispatch();
  const handleFileUpload = (e: React.ChangeEvent<HTMLInputElement>) => {
    const files = e.target.files;
    if (files) {
      setFile(files[0]);
      setUrl(URL.createObjectURL(files[0]));
    }
  };

  useEffect(() => {
    return () => {
      URL.revokeObjectURL(url);
    }
  }, [url]);

  const handleAdd = async () => {
    if (categoryName === '') {
      setIsValid(false);
      return;
    }
    const formData = new FormData();
    if (file) {
      formData.append('image', file);
    }
    if (parentId) {
      formData.append('parentId', parentId);
    }
    try {
      formData.append('categoryName', categoryName);
      await trigger(formData).unwrap();
      dispatch(setNotify({
        type: 'success', message: 'Thêm danh mục thành công'
      }))
      if (refetch) refetch();
      handleClose();
    } catch (error) {
      console.log(error);
      dispatch(setNotify({
        type: 'error', message: 'Thêm không thành công'
      }))
    }
  }
  const renderTooltip = (props: TooltipProps) => (
    <Tooltip id="custom-tooltip" {...props} className="custom-tooltip">
      * Không được bỏ trống
    </Tooltip>
  );
  return <>
    <Modal show={show} onHide={handleClose} centered>
      <Modal.Header closeButton>
        <h5>Thêm danh mục</h5>
      </Modal.Header>
      <Modal.Body>
        <div className="w-100 d-flex gap-2 align-items-center">
          <>
            {url === '' ? <><input onChange={handleFileUpload} id="category-img" type="file" accept="image/*" style={{ display: 'none' }} />
              <label htmlFor="category-img" className="d-flex align-items-center primary mt-1">
                <div className="image-color p-2">
                  <div className="icon-image-insert">
                    <FontAwesomeIcon icon={faImage} fontSize={30} />
                    <FontAwesomeIcon className="icon-plus-image" icon={faPlus} />
                  </div>
                </div>
              </label></> : <img src={url} width={50} height={50} />}
          </>
          <div className="form-group">
            <OverlayTrigger placement="bottom"
              overlay={renderTooltip}
              show={!isValid}>
              <input className="form-control no-shadow"
                placeholder="Tên danh mục"
                value={categoryName} onChange={(e) => { setCategoryName(e.target.value); setIsValid(true) }} />

            </OverlayTrigger>
          </div>

        </div>
      </Modal.Body>
      <Modal.Footer>
        <div className="w-100 d-flex justify-content-end gap-3">
          <Button variant="secondary" onClick={handleClose}>
            Đóng
          </Button>
          <Button variant="primary" onClick={handleAdd}>
            Thêm
          </Button>
          <div>
          </div>
        </div>

      </Modal.Footer>
    </Modal>
    {isLoading && <ModalLoading loading={isLoading} />}
  </>
}

export default CreateCategoryModal;