import React from 'react'
import { Navigate } from 'react-router';
import { useServerStatus } from '../../context/ServerStatusContext';

type Props = {}

const DashboardPage = (props: Props) => {
    const { setServerStatus, setCasInfo, serverStatus } = useServerStatus();

    if (serverStatus !== 'ACTIVATE') {
        return <Navigate to="/ca-registration" replace />;
    } else {
        return <Navigate to="/ca-management" replace />;
    }
    
}

export default DashboardPage