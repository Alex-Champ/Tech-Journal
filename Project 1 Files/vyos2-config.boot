firewall {
    all-ping enable
    broadcast-ping disable
    config-trap disable
    ipv6-receive-redirects disable
    ipv6-src-route disable
    ip-src-route disable
    log-martians enable
    name LAN-WAN {
        default-action drop
        enable-default-log
        rule 1 {
            action accept
            state {
                established enable
            }
        }
        rule 10 {
            action accept
            destination {
                port 80
            }
            protocol tcp
        }
        rule 20 {
            action accept
            protocol icmp
        }
        rule 30 {
            action accept
            destination {
                port 443
            }
            protocol tcp
        }
        rule 50 {
            action accept
            destination {
                port 53
            }
            protocol tcp_udp
        }
        rule 60 {
            action accept
            description "Enable TraceRoute"
            protocol udp
        }
    }
    name OPT-WAN {
        default-action drop
    }
    name WAN-LAN {
        default-action drop
        enable-default-log
        rule 1 {
            action accept
            state {
                established enable
            }
        }
        rule 10 {
            action accept
            description "Allow SSH"
            destination {
                port 22
            }
            protocol tcp
        }
        rule 20 {
            action accept
            description "Open Traffic on port 80 to Web Server"
            destination {
                port 80
            }
            protocol tcp
        }
        rule 30 {
            action accept
            description "Enable TracePath"
            protocol icmp
        }
    }
    name WAN-OPT {
        default-action drop
    }
    receive-redirects disable
    send-redirects enable
    source-validation disable
    syn-cookies enable
    twa-hazards-protection disable
}
high-availability {
    vrrp {
        group langroup0 {
            interface eth1
            virtual-address 10.0.5.1/24
            vrid 160
        }
        group optgroup0 {
            interface eth2
            virtual-address 10.0.6.1/24
            vrid 170
        }
        group wangroup0 {
            interface eth0
            virtual-address 10.0.17.70/24
            vrid 150
        }
    }
}
interfaces {
    ethernet eth0 {
        address 10.0.17.50/24
        description WAN
        hw-id 00:50:56:b3:2f:c7
    }
    ethernet eth1 {
        address 10.0.5.3/24
        description LAN
        hw-id 00:50:56:b3:11:b7
    }
    ethernet eth2 {
        address 10.0.6.3/24
        description OPT
        hw-id 00:50:56:b3:b5:24
    }
    loopback lo {
    }
}
nat {
    destination {
        rule 10 {
            description "Port Forward: HTTP to web01"
            destination {
                port 80
            }
            inbound-interface eth0
            protocol tcp
            translation {
                address 10.0.5.100
            }
        }
        rule 20 {
            description "Port Forward: SSH to web01"
            destination {
                port 22
            }
            inbound-interface eth0
            protocol tcp
            translation {
                address 10.0.5.100
            }
        }
    }
    source {
        rule 100 {
            outbound-interface eth0
            source {
                address 10.0.5.0/24
            }
            translation {
                address masquerade
            }
        }
    }
}
protocols {
    static {
        route 0.0.0.0/0 {
            next-hop 10.0.17.2 {
            }
        }
    }
}
service {
    dns {
        forwarding {
            allow-from 10.0.17.0/24
            allow-from 10.0.5.0/24
            listen-address 10.0.5.3
            listen-address 10.0.17.50
            listen-address 10.0.5.1
            name-server 10.0.17.2
        }
    }
    ssh {
        port 22
    }
}
system {
    config-management {
        commit-revisions 100
    }
    console {
    }
    host-name vyos
    login {
        user vyos {
            authentication {
                encrypted-password $6$Tf8fh/GcIL1$SZCjd50gLgpIHm3x.60qVFDKe9hhxXOtJxRVNOKmb7LFBbsvVqwTvF4nyl8HRPXfzsK6cng7xsNR4LlvL.NcY0
                plaintext-password ""
            }
        }
    }
    name-server 10.0.17.2
    ntp {
        server 0.pool.ntp.org {
        }
        server 1.pool.ntp.org {
        }
        server 2.pool.ntp.org {
        }
    }
    syslog {
        global {
            facility all {
                level info
            }
            facility protocols {
                level debug
            }
        }
    }
}
zone-policy {
    zone LAN {
        default-action drop
        from WAN {
            firewall {
                name WAN-LAN
            }
        }
        interface eth1
    }
    zone OPT {
        default-action drop
        from WAN {
            firewall {
                name WAN-OPT
            }
        }
        interface eth2
    }
    zone WAN {
        default-action drop
        from LAN {
            firewall {
                name LAN-WAN
            }
        }
        from OPT {
            firewall {
                name OPT-WAN
            }
        }
        interface eth0
    }
}


// Warning: Do not remove the following line.
// vyos-config-version: "broadcast-relay@1:cluster@1:config-management@1:conntrack@1:conntrack-sync@1:dhcp-relay@2:dhcp-server@5:dhcpv6-server@1:dns-forwarding@3:firewall@5:https@2:interfaces@11:ipoe-server@1:ipsec@5:l2tp@3:lldp@1:mdns@1:nat@5:ntp@1:pppoe-server@3:pptp@2:qos@1:quagga@6:salt@1:snmp@1:ssh@1:sstp@2:system@18:vrrp@2:vyos-accel-ppp@2:wanloadbalance@3:webgui@1:webproxy@2:zone-policy@1"
// Release version: 1.3-rolling-202007010117
