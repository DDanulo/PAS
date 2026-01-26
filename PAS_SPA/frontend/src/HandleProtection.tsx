import { Navigate, useLocation } from 'react-router-dom';
import { useAuth } from './context/LoggedUserContext';

export enum RoleEnum {
    ADMIN = 'ADMIN',
    CLIENT = 'CLIENT',
}

interface HandleProtectionProperties {
    children: Element;
    allowedRoles?: RoleEnum[];
}

const ProtectedRoute = ({ children, allowedRoles }: HandleProtectionProperties) => {
    const { isAuthenticated, userRole } = useAuth();
    const location = useLocation();

    if (!isAuthenticated) {
        return <Navigate to="/login" state={{ from: location }} replace />;
    }

    const hasPermission = !allowedRoles || allowedRoles.includes(userRole as RoleEnum);

    if (!hasPermission) {
        return <Navigate to="/users" replace />;
    }

    return children;
};

export default ProtectedRoute;