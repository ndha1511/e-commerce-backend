import { createBrowserRouter, Navigate } from "react-router-dom";
import UserLayout from "../layout/user/UserLayout";
import Home from "../pages/user/home/Home";
import Cart from "../pages/user/cart/Cart";
import Payment from "../pages/user/payment/Payment";
import Login from "../pages/user/auth/login/Login";
import ProductDetail from "../pages/user/product/ProductDetail";
import Account from "../pages/user/account/Account";
import Profile from "../pages/user/account/profile/Profile";
import Address from "../pages/user/account/address/Address";
import Password from "../pages/user/account/password/Password";
import Purchase from "../pages/user/account/purchase/Purchase";
import Notification from "../pages/user/account/notification/Notification";
import Voucher from "../pages/user/account/voucher/Voucher";
import InsertProduct from "../pages/admin/product/InsertProduct";
import AuthLayout from "../layout/user/auth/AuthLayout";
import Register from "../pages/user/auth/register/Register";
import VerifyEmail from "../pages/user/auth/register/VerifyEmail";
import ForgotPassword from "../pages/user/auth/reset-password/ForgotPassword";
import ResetPassword from "../pages/user/auth/reset-password/ResetPassword";
import AdminLayout from "../layout/admin/AdminLayout";
import Product from "../pages/admin/product/Product";
import ProductAttribute from "../pages/admin/product/ProductAttribute";
import CreatePromotion from "../pages/admin/promotion/CreatePromotion";
import ProductStock from "../pages/admin/product/ProductStock";
import ProductImport from "../pages/admin/product/ProductImport";
import Category from "../pages/admin/category/Category";
import CategoryUser from "../pages/user/category/Category";
import PurchaseAdmin from "../pages/admin/purchase/PurchaseAdmin";
import Brand from "../pages/admin/brand/CreateBrand";
import Batch from "../pages/admin/product/Batch";
import BrandList from "../pages/admin/brand/BrandList";
import CreateEmployee from "../pages/admin/employee/CreateEmployee";
import EmployeeList from "../pages/admin/employee/EmployeeList";
import App from "../App";
import NotFound from "../pages/not-found/NotFound";
import Message from "../pages/admin/message/Message";

export const router = createBrowserRouter([
    {
        path: "*",
        element: <App><UserLayout><NotFound /></UserLayout></App>
    },
    {
        path: "/",
        element: <App><UserLayout><Home/></UserLayout></App>
    },
    {
        path: "/cart",
        element: <UserLayout><Cart/></UserLayout>
    },
    {

        path: "/payment",
        element: <UserLayout><Payment/></UserLayout>
    }, 
    {
        path: "/auth/login",
        element: <AuthLayout><Login/></AuthLayout>
    },
    {
        path: "/auth/register",
        element: <AuthLayout><Register/></AuthLayout>
    },
    {
        path: "/auth/verify-email",
        element: <AuthLayout><VerifyEmail/></AuthLayout>
    },
    {
        path: "/auth/forgot-password",
        element: <AuthLayout><ForgotPassword/></AuthLayout>
    },
    {
        path: "/auth/create-new-password",
        element: <AuthLayout><ResetPassword/></AuthLayout>
    },
    {
        path: "/user",
        element: <Navigate to={"account"}/>
    },
    {
        path: "/user/account",
        element: <Navigate to={"profile"}/>
    },
    {
        path: "/user/account/profile",
        element: <UserLayout><Account><Profile/></Account></UserLayout>
    },
    {
        path: "/user/account/address",
        element: <UserLayout><Account><Address/></Account></UserLayout>
    },
    {
        path: "/user/account/password",
        element: <UserLayout><Account><Password/></Account></UserLayout>
    },
    {
        path: "/user/purchase",
        element: <App><UserLayout><Account><Purchase/></Account></UserLayout></App>
        
    },
    {
        path: "/user/notifications",
        element: <UserLayout><Account><Notification/></Account></UserLayout>
    },
    {
        path: "/user/vouchers",
        element: <UserLayout><Account><Voucher/></Account></UserLayout>
    }
    ,
    {
        path: "/product/:key",
        element: <App><UserLayout><ProductDetail/></UserLayout></App>
    },
    {
        path: "/:categoryPath",
        element: <UserLayout><CategoryUser/></UserLayout>
    },
    {

        path: "/admin",
        element: <AdminLayout><CreatePromotion/></AdminLayout>
    },
    {
        path: "/admin/product/create",
        element: <AdminLayout><InsertProduct/></AdminLayout>
    },
    {
        path: "/admin/products",
        element: <AdminLayout><Product/></AdminLayout>
    },
    {
        path: "/admin/products/attribute",
        element: <AdminLayout><ProductAttribute/></AdminLayout>
    },
    {
        path: "/admin/product/stock",
        element: <AdminLayout><ProductStock/></AdminLayout>
    },
    {
        path: "/admin/product/import",
        element: <AdminLayout><ProductImport/></AdminLayout>
    },
    {
        path: "/admin/product/batch",
        element: <AdminLayout><Batch/></AdminLayout>
    },
    {
        path: "/admin/categories",
        element: <AdminLayout><Category/></AdminLayout>
    },
    {
        path: "/admin/purchase",
        element: <App><AdminLayout><PurchaseAdmin/></AdminLayout></App>
    },
    {
        path: "/admin/brand",
        element: <AdminLayout><Brand/></AdminLayout>
    },
    {
        path: "/admin/brands",
        element: <AdminLayout><BrandList/></AdminLayout>
    },
    {
        path: "/admin/employee/create",
        element: <AdminLayout><CreateEmployee/></AdminLayout>
    },
    {
        path: "/admin/employees",
        element: <AdminLayout><EmployeeList/></AdminLayout>
    },
    {
        path: "/admin/messages",
        element: <App><AdminLayout><Message/></AdminLayout> </App>
    },


]);