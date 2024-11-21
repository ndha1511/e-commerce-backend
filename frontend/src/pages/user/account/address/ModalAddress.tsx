
import { Button, Modal } from "react-bootstrap";
import { setNotify } from "../../../../rtk/slice/notify-slice";
import { UserAddressDto } from "../../../../dtos/request/address/user-address-dto";
import { useEffect, useState } from "react";
import { useDeleteUserAddressMutation, useGetAddressByUserIdQuery, useUpdateUserAddressMutation } from "../../../../services/address.service";
import { Action } from "../../../../components/address/ModalAddress";
import { useDispatch } from "react-redux";
import { useCheckLoginQuery } from "../../../../services/auth.service";
import CreateAddressModal from "../../../../components/address/CreateAddressModal";
interface ModalAddressProps {
    show: boolean;
    handleClose: () => void;
    refetch?: () => void;
}
function ModalAddress({ show, handleClose, refetch }: ModalAddressProps) {
    const { data: user, isSuccess: loginSuccess } = useCheckLoginQuery();
    const { data: address, refetch: addressRefetch, isSuccess } = useGetAddressByUserIdQuery(user?.data?.id || "", {
        skip: !loginSuccess || !user?.data?.id,
    });
    const [addressId, setAddressId] = useState<string | null>('');
    const [modalAdd, setModalAdd] = useState(false);
    const [addressArr, setAddressArr] = useState<UserAddressDto[]>([]);
    const [action, setAction] = useState<Action>('null');
    const [updateAddress] = useUpdateUserAddressMutation();
    const [deleteAddress] = useDeleteUserAddressMutation();
    const dispatch = useDispatch();
    useEffect(() => {
        if (isSuccess && address?.data) {
            // Sắp xếp địa chỉ để đưa những địa chỉ mặc định lên đầu
            const sortedAddresses = [...address.data].sort((a, b) => {
                return (b.addressDefault ? 1 : 0) - (a.addressDefault ? 1 : 0);
            });
            setAddressArr(sortedAddresses);
        }
    }, [isSuccess, address]);
    const handleUpdateAddress = async (addressId: string) => {
        const addressUpdate = address?.data.find((addr: UserAddressDto) => addr.id === addressId);
        try {
            await updateAddress({
                updateUserAddress: {
                    userId: user?.data?.id || '',
                    receiverName: addressUpdate?.receiverName || '',
                    phoneNumber: addressUpdate?.phoneNumber || '',
                    addressDefault: true,
                    addressDetail: addressUpdate?.addressDetail || '',
                    district: addressUpdate?.district || '',
                    province: addressUpdate?.province || '',
                    ward: addressUpdate?.ward || '',
                },
                addressId: addressId || ''
            }).unwrap();
            dispatch(setNotify({
                message: 'Cập nhật thành công',
                type: 'success'
            }))
            addressRefetch();
        } catch (error) {
            console.error(error);
            dispatch(setNotify({
                message: 'Cập nhật không thành công',
                type: 'error'
            }))
        }
    }
    const handleDeleteAddress = async (addressId: string) => {
        try {
            await deleteAddress(addressId);
            dispatch(setNotify({
                message: 'Thao tác thành công',
                type: 'success'
            }))
         
        } catch (error) {
            dispatch(setNotify({
                message: 'Thao tác không thành công',
                type: 'error'
            }))
        }
    }
    return (
        <div>
            <Modal show={show} onHide={handleClose}>
                <Modal.Header closeButton>
                    <Modal.Title><span className="text-medium">Địa chỉ của tôi</span></Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <div className="">

                        {addressArr?.map((address, index) => (
                            <div key={index} className="mt-3 d-flex align-items-center justify-content-between border-bottom">
                                <div className="d-flex flex-column p-3 ">
                                    <span className="mb-1">
                                        {address.receiverName}
                                        <span className="border-start border-2 ps-2 p-1 ms-1 text-light-cs">{address.phoneNumber}</span>
                                    </span>
                                    <span className="mb-1 text-light-cs">{address.addressDetail}</span>
                                    <span className="mb-1 text-light-cs">{address.ward}, {address.district}, {address.province}</span>
                                    {address.addressDefault && <div className="mt-1">
                                        <button className="btn-default">Mặc định</button>
                                    </div>}
                                </div>
                                <div className="mb-2 d-flex flex-column gap-3 align-items-center">
                                    <div>
                                        <button className="btn-address me-3" onClick={() => { setModalAdd(true); setAction("update"); setAddressId(address.id || '') }}>Cập nhật</button>
                                        <button className="btn-address" onClick={() => handleDeleteAddress(address.id || '')}>Xóa</button>
                                    </div>
                                    {!address.addressDefault && <div className="mt-2">
                                        <button
                                            className="btn-setting"
                                            onClick={() => handleUpdateAddress(address.id || '')}
                                        >
                                            Thiết lập mặc định
                                        </button>
                                    </div>}
                                </div>
                            </div>
                        ))}
                        <div className="d-flex justify-content-between align-items-center   mt-3">
                            <button className="button-flex button-hover bg-white border text-medium d-flex gap-2 p-2 pe-3 text-muted"
                                onClick={() => { setModalAdd(true); setAction("save"); }}
                            >
                                <i className="bi bi-plus-lg"></i>
                                Thêm địa chỉ mới
                            </button>
                        </div>
                    </div>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={handleClose}>
                        Cancel
                    </Button>
                    <Button variant="primary" onClick={handleClose}>
                        Confirm
                    </Button>
                </Modal.Footer>
            </Modal>
            {modalAdd && <CreateAddressModal  show={modalAdd} handleClose={() => setModalAdd(false)} refetch={addressRefetch} />}
        </div>
    );
}

export default ModalAddress;