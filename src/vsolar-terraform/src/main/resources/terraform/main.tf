terraform {
  required_providers {
    vsphere = {
      source  = "hashicorp/vsphere"
      version = ">= 1.0.0"
    }
  }
  required_version = ">= 0.13"
}

provider "vsphere" {
  user                 = var.vsphereUser
  password             = var.vspherePassword
  vsphere_server       = var.vsphereServer
  allow_unverified_ssl = true
}

data "vsphere_datacenter" "dc" {
  name = var.vsphereDatacenter
}

data "vsphere_host" "host" {
  name          = var.vsphereHost
  datacenter_id = data.vsphereDatacenter.dc.id
}

data "vsphere_datastore" "ds" {
  name          = var.vmDatastore
  datacenter_id = data.vsphere_datacenter.dc.id
}

data "vsphere_compute_cluster" "cluster" {
  name          = var.vmCluster
  datacenter_id = data.vsphere_datacenter.dc.id
}

data "vsphere_virtual_machine" "template" {
  name          = var.vmTemplate
  datacenter_id = data.vsphere_datacenter.dc.id
}

data "vsphere_network" "network" {
  name          = var.vmNetwork
  datacenter_id = data.vsphere_datacenter.dc.id
}

resource "vsphere_virtual_machine" "example_vm" {
  name             = var.vmName
  datastore_id     = data.vsphere_datastore.ds.id
  resource_pool_id = data.vsphere_compute_cluster.cluster.resource_pool_id
  memory           = var.vmMemory
  num_cpus         = var.vmCpu
  guest_id         = data.vsphere_virtual_machine.template.guest_id

  network_interface {
    network_id = data.vsphere_network.network.id
  }


  disk {
    label = data.vsphere_virtual_machine.template.disks.0.label
    size  = data.vsphere_virtual_machine.template.disks.0.size
  }

  clone {
    template_uuid = data.vsphere_virtual_machine.template.id
  }

}
