import { Table } from "react-bootstrap";
import { useGetEmployeeQuery } from "../../../services/user.service";
import { pageQueryHanlder } from "../../../utils/query-handler";

function EmployeeList() {
    const params = pageQueryHanlder(1, 40)
    const { data } = useGetEmployeeQuery(params);
    const employeeList = data?.data.items;
    console.log(data);
    return (
        <div className="p-3 bg-light">
            <span className="text-medium">Danh sách nhân viên</span>

            <Table className="mb-0 table-bordered table-responsive custom-table-category mt-3">

                <thead>
                    <tr>
                        <th>Hình ảnh</th>
                        <th>Email</th>
                        <th>Giới tính</th>
                        <th>Số điện thoại</th>
                        <th>Ngày sinh</th>
                        <th>Ngày tạo</th>
                    </tr>
                </thead>
                    <tbody>
                        {employeeList?.map((e) => (
                            <tr key={e.id}>
                                <td style={{ width: '15%' }}><img src={e.avatar} alt="" width={60} height={60} /></td>
                                <td style={{ width: '20%' }}>{e.email || ''}</td>
                                <td style={{ width: '10%' }}>{e.gender || ''}</td>
                                <td style={{ width: '15%' }}>{e.phoneNumber || '00000000'}</td>
                                <td style={{ width: '20%' }}>{e.dateOfBirth && e.dateOfBirth.toString()}</td>
                                <td style={{ width: '20%' }} >{e.createdAt && e.createdAt.toString()}</td>
                            </tr>
                        ))}

                    </tbody>
            </Table>

        </div>
    );
}

export default EmployeeList;