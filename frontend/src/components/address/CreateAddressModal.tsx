import { Button, Modal } from "react-bootstrap";
import { useAddUserAddressMutation, useGetDistrictsQuery, useGetProvincesQuery, useGetWardsQuery } from "../../services/address.service";
import Select from 'react-select'
import { useEffect, useState } from "react";
import { useCheckLoginQuery } from "../../services/auth.service";
import { useDispatch } from "react-redux";
import { setNotify } from "../../rtk/slice/notify-slice";

interface Props {
  show: boolean;
  handleClose: () => void;
  refetch: () => void;
  addressId?: string;
}

function CreateAddressModal({ show, handleClose, refetch }: Props) {
    const [provinceID, setProvinceId] = useState<number>(0);
    const [districtID, setDistrictId] = useState<number>(0);
    const [wardID, setWardId] = useState<number>(0);
    const [addAddress] = useAddUserAddressMutation();
    const [name, setName] = useState<string>('');
    const [phone, setPhone] = useState<string>('');
    const [detail, setDetail] = useState<string>('');
    const { data: provinces } = useGetProvincesQuery();
    const { data: user } = useCheckLoginQuery();
    const { data: districts, refetch: districtRefetch } = useGetDistrictsQuery(provinceID, {
        skip: provinceID === 0,
    });
    const { data: wards, refetch: wardRefetch } = useGetWardsQuery(districtID, {
        skip: districtID === 0,
    });
    const dispatch = useDispatch();
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
            dispatch(setNotify({
                type: 'success', message: 'Thao tác thành công'
              }))
        } catch (error) {
            console.log(error);
            dispatch(setNotify({
                type: 'success', message: 'Thao tác không thành công'
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
                <Button variant="primary" onClick={handleAddAddress}>
                    Lưu
                </Button>

            </Modal.Footer>
        </Modal>
    );
}

export default CreateAddressModal;