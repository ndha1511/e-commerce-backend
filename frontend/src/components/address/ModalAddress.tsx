import { Button, Modal } from "react-bootstrap";
import { useAddUserAddressMutation, useGetDistrictsQuery, useGetProvincesQuery, useGetUserAddressQuery, useGetWardsQuery, useUpdateUserAddressMutation } from "../../services/address.service";
import Select from 'react-select'
import { useEffect, useState } from "react";
import { useCheckLoginQuery } from "../../services/auth.service";
import { UserAddressDto } from "../../dtos/request/address/user-address-dto";
import { useDispatch } from "react-redux";
import { setNotify } from "../../rtk/slice/notify-slice";

export type Action = "save" | "update" | "null";
interface Props {
  show: boolean;
  handleClose: () => void;
  refetch: () => void;
  action?: Action;
  addressId?: string;
}

const ModalAddress = ({ show, handleClose, refetch, action, addressId }: Props) => {
  const [provinceID, setProvinceId] = useState<number>(0);
  const [districtID, setDistrictId] = useState<number>(0);
  const [wardID, setWardId] = useState<number>(0);
  const [addAddress] = useAddUserAddressMutation();
  const [name, setName] = useState<string>('');
  const [phone, setPhone] = useState<string>('');
  const [detail, setDetail] = useState<string>('');
  const [trigger] = useUpdateUserAddressMutation();
  const { data: provinces, isSuccess: provinceSuccess } = useGetProvincesQuery();
  const { data: user, isSuccess: loginSuccess } = useCheckLoginQuery();
  const [fetchAllSuccess, setFetchAllSuccess] = useState(false);
  const { data: districts, refetch: districtRefetch, isSuccess: districtsSuccess } = useGetDistrictsQuery(provinceID, {
    skip: provinceID === 0,
  });
  const { data: wards, refetch: wardRefetch, isSuccess: wardsSuccess } = useGetWardsQuery(districtID, {
    skip: districtID === 0,
  });
  const { data: dataUserAddress, isSuccess: addressSuccess, refetch: refetchAddress } = useGetUserAddressQuery(user?.data?.id || "", {
    skip: !loginSuccess || !user?.data?.id || !provinceSuccess
  });
  const address = dataUserAddress?.data.find((addr: UserAddressDto) => addr.id === addressId);
  const dispatch = useDispatch();
  useEffect(() => {

    if (!fetchAllSuccess) {
      if (provinceSuccess) {

        setProvinceId(provinces?.data.find(p => p.ProvinceName === address?.province)?.ProvinceID || 0);
      }
      if (provinceSuccess && districtsSuccess) {

        setDistrictId(districts?.data?.find(d => d.DistrictName === address?.district)?.DistrictID ?? 0);
      }
      if (provinceSuccess && districtsSuccess && wardsSuccess) {

        setWardId(wards?.data.find(w => w.WardName === address?.ward)?.WardCode || 0);
        setName(address?.receiverName || '');
        setPhone(address?.phoneNumber || '');
        setDetail(address?.addressDetail || '');
        setFetchAllSuccess(true);

      }
    }


  }, [provinceSuccess, addressSuccess, districtsSuccess, wardsSuccess, fetchAllSuccess])

  const provinceOptions = provinces?.data.map(p => ({
    value: p.ProvinceID,
    label: p.ProvinceName,
  })) || [];

  const districtOptions = districts?.data.map(d => ({
    value: d.DistrictID,
    label: d.DistrictName,
  })) || [];

  const [wardOptions, setWardOptions] = useState<{ value: number, label: string }[]>([]);

  useEffect(() => {
    setWardOptions(wards?.data.map(w => ({
      value: w.WardCode,
      label: w.WardName,
    })) || []);
  }, [wards]);

  useEffect(() => {
    if (provinceID !== 0) {
      districtRefetch();
      setDistrictId(0);
    }
  }, [provinceID]);

  useEffect(() => {
    if (districtID !== 0) {
      wardRefetch();
      setWardId(0);
    } else {
      setWardOptions([]);
    }
  }, [districtID]);

  const handleAddAddress = async () => {
    try {
      await addAddress({
        addressDefault: false,
        district: districts?.data.find(d => d.DistrictID === districtID)?.DistrictName || '',
        province: provinces?.data.find(p => p.ProvinceID === provinceID)?.ProvinceName || '',
        ward: wards?.data.find(w => w.WardCode === wardID)?.WardName || '',
        addressDetail: detail,
        userId: user?.data?.id || '',
        phoneNumber: phone,
        receiverName: name
      }).unwrap();
      refetch();
      handleClose();
    } catch (error) {
      console.log(error);
    }
  }
  const handleUpdate = async () => {
    try {
      await trigger({
        updateUserAddress: {
          userId: user?.data?.id || '',
          receiverName: name || '',
          phoneNumber: phone || '',
          addressDefault: address?.addressDefault ?? false,
          addressDetail: detail || '',
          district: districts?.data.find(d => d.DistrictID === districtID)?.DistrictName || '',
          province: provinces?.data.find(p => p.ProvinceID === provinceID)?.ProvinceName || '',
          ward: wards?.data.find(w => w.WardCode === wardID)?.WardName || '',
        },
        addressId: addressId || ''
      }).unwrap();
      refetch();
      refetchAddress();
      handleClose();
      dispatch(setNotify({
        type: 'success', message: 'Cập nhật thành công'
      }))
    } catch (error) {
      dispatch(setNotify({
        type: 'error', message: 'Cập nhật Không thành công'
      }))
    }
  }
  return (
    <Modal show={show} onHide={handleClose}>
      <Modal.Header closeButton>
        <h6>Thêm địa chỉ</h6>
      </Modal.Header>
      <Modal.Body>
        <div className="d-flex w-100 flex-column gap-3">
          <span>Địa chỉ</span>
          <Select
            options={provinceOptions}
            placeholder="Tỉnh/Thành phố"
            onChange={(e) => {
              setProvinceId(e?.value || 0);
            }}
            value={provinceOptions.find(option => option.value === provinceID) || null}
          />

          <Select
            options={districtOptions}
            placeholder="Quận/Huyện"
            onChange={(e) => {
              setDistrictId(e?.value || 0);
            }}
            value={districtOptions.find(option => option.value === districtID) || null}
          />

          <Select
            options={wardOptions}
            placeholder="Phường/Xã"
            onChange={(e) => setWardId(e?.value || 0)}
            value={wardOptions.find(option => option.value === wardID) || null}
          />

          <input
            value={detail}
            onChange={e => setDetail(e.target.value)}
            placeholder="Số nhà, tên đường, hẻm, ngõ, ngách..."
            className="form-control no-shadow text-normal"
          />
        </div>
        <div className="d-flex w-100 flex-column gap-3 mt-3">
          <span>Thông tin người nhận</span>
          <input
            value={name}
            onChange={(e) => setName(e.target.value)}
            placeholder="Tên người nhận"
            className="form-control no-shadow text-normal"
          />
          <input
            value={phone}
            onChange={(e) => setPhone(e.target.value)}
            placeholder="Số điện thoại người nhận"
            className="form-control no-shadow text-normal"
          />
        </div>
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={handleClose}>
          Close
        </Button>
        <Button variant="primary" onClick={action === 'update' ? handleUpdate : handleAddAddress}>
          Lưu
        </Button>

      </Modal.Footer>
    </Modal>
  );
}

export default ModalAddress;
